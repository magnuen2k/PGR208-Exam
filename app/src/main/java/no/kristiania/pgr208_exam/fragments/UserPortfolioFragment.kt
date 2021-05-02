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
import no.kristiania.pgr208_exam.PortfolioStatement
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.adapters.UserPortfolioAdapter
import no.kristiania.pgr208_exam.data.API
import no.kristiania.pgr208_exam.data.CoinCapService
import no.kristiania.pgr208_exam.databinding.UserPortfolioFragmentBinding
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.viewmodels.OverviewViewModel

class UserPortfolioFragment : Fragment(R.layout.user_portfolio_fragment) {

    private lateinit var binding: UserPortfolioFragmentBinding

    private var portfolios = mutableListOf<PortfolioStatement>()

    val overviewModel = OverviewViewModel()

    private lateinit var adapter: UserPortfolioAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = UserPortfolioFragmentBinding.bind(view)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        adapter = UserPortfolioAdapter(portfolios)
        binding.recyclerView.adapter = adapter

        overviewModel.allCcAssets.observe(this, Observer {
            portfolios.clear()
            lifecycleScope.launch(Dispatchers.IO) {
                var userPortfolios =
                    DataBase.getDatabase(requireContext()).getUserPortfolioDAO().fetchAll()
                userPortfolios.forEach { portfolio ->
                    val specificCcData =
                        overviewModel.allCcAssets.value?.data?.find { it.symbol.equals(portfolio.symbol) }
                    if (specificCcData != null) {
                        Log.d("INFO", "xyx values ${specificCcData.symbol}")
                        portfolios.add(
                            PortfolioStatement(
                                portfolio.symbol,
                                portfolio.volume,
                                specificCcData.priceUsd.toString(),
                                "${portfolio.volume.toDouble() * (specificCcData.priceUsd!!.toDouble())}"
                            )
                        )
                    }
                }
            }
            adapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        overviewModel.getAssetOverview()
    }

}