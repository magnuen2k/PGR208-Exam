package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.kristiania.pgr208_exam.PortfolioStatement
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.adapters.UserPortfolioAdapter
import no.kristiania.pgr208_exam.data.API
import no.kristiania.pgr208_exam.data.CoinCapService
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.data.domain.SpecificCcData
import no.kristiania.pgr208_exam.databinding.UserPortfolioFragmentBinding
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio
import no.kristiania.pgr208_exam.viewmodels.OverviewViewModel

class UserPortfolioFragment : Fragment(R.layout.user_portfolio_fragment) {

    private lateinit var binding: UserPortfolioFragmentBinding

    private var portfolios = mutableListOf<PortfolioStatement>()

    val overviewModel = OverviewViewModel()

    private lateinit var adapter: UserPortfolioAdapter

    private val coinCapService: CoinCapService = API.COIN_CAP_SERVICE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = UserPortfolioFragmentBinding.bind(view)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        adapter = UserPortfolioAdapter(portfolios)
        binding.recyclerView.adapter = adapter

        overviewModel.allCcAssets.observe(this, Observer {
            portfolios.clear()
            lifecycleScope.launch(Dispatchers.IO) {
                var userPortfolios = DataBase.getDatabase(requireContext()).getUserPortfolioDAO().fetchAll()
                userPortfolios.forEach {portfolio ->
                    Log.d("INFO", "${portfolio.symbol} Volume: ${portfolio.volume}")
                    userPortfolios.forEach {uPo ->
                        val xyz = overviewModel.allCcAssets.value?.data?.find {it.symbol.equals(uPo.symbol)}
                        if (xyz != null) {
                            Log.d("INFO", "xyx values ${xyz.symbol}")
                            portfolios.add(PortfolioStatement(uPo.symbol, uPo.volume, xyz.priceUsd.toString(), "${uPo.volume.toDouble() * (xyz.priceUsd!!.toDouble())}"))
                        }
                    }
                }
                /*ccOverviews.clear()
            ccOverviews.addAll(newCcData.data)
            adapter.notifyDataSetChanged()*/

                /*Log.d("STATEMENT", "${p.symbol} Volume: ${p.volume}")
                    val overview = overviews[overviews.indexOfFirst { it.symbol == p.symbol}]
                    Log.d("STATEMENT", "${overview.id} ${overview.symbol}")
                    //val totalValue = p.volume.toDouble() * overview.priceUsd!!.toDouble()
                    //testList.add(PortfolioStatement(p.symbol, p.volume, overview.priceUsd.toString(), "$totalValue"))*/
            }
            adapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        overviewModel.getAssetOverview()
    }

}