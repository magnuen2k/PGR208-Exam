package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionActivity
import no.kristiania.pgr208_exam.databinding.TransactionOptionFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.TransactionOptionViewModel
import java.util.*

class TransactionOptionFragment : Fragment(R.layout.transaction_option_fragment) {

    private lateinit var binding: TransactionOptionFragmentBinding
    private lateinit var viewModel: TransactionOptionViewModel

    lateinit var currency: String
    lateinit var symbol: String
    lateinit var recentRate: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = TransactionOptionFragmentBinding.bind(view)

        viewModel = ViewModelProvider(requireActivity()).get(TransactionOptionViewModel::class.java)

        val bundle = arguments
        bundle?.let {
            symbol = bundle.getString("symbol")!!
            currency = bundle.getString("currency")!!.toLowerCase(Locale.ROOT)
            recentRate = bundle.getString("recentRate")!!
        }


        mountObservers()


        binding.buyBtn.setOnClickListener {
            (context as TransactionActivity).supportFragmentManager.beginTransaction().replace(
                R.id.transactionFragmentContainer,
                TransactionBuyFragment().apply { arguments = Bundle().apply {
                    putString("currency", currency)
                    putString("recentRate", recentRate)
                    putString("symbol", symbol)
                } },
                "TransactionBuyFragment"
            ).addToBackStack("buy").commit()
        }

        binding.sellBtn.setOnClickListener {
            (context as TransactionActivity).supportFragmentManager.beginTransaction().replace(
                R.id.transactionFragmentContainer,
                TransactionSellFragment().apply { arguments = Bundle().apply {
                    putString("currency", currency)
                    putString("recentRate", recentRate)
                    putString("symbol", symbol)
                } },
                "TransactionSellFragment"
            ).addToBackStack("sell").commit()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUserUsd()
        viewModel.getCurrency(currency)
        viewModel.getChart(currency)
    }

    fun mountObservers() {
        viewModel.currency.observe(this, Observer { data ->
            symbol = data.symbol ?: symbol
            currency = data.id ?: currency
            recentRate = data.priceUsd ?: recentRate
            viewModel.getPortfolio(symbol)
        })

        viewModel.userPortfolio.observe(this, Observer { portfolio ->
            binding.ccUserAmount.text = "You have ${portfolio.volume} ${portfolio.symbol}"
            binding.toUsdCalculation.text = "${portfolio.volume} x ${recentRate}"
            binding.ccValueInUsd.text = "Value ${portfolio.volume.toDouble() * recentRate.toDouble()} USD"
            binding.sellBtn.isEnabled = portfolio.volume.toDouble() > 0
        })

        viewModel.userUsd.observe(this, Observer { usdPortfolio ->
            binding.buyBtn.isEnabled = usdPortfolio.volume.toDouble() > 0
        })

        viewModel.chart.observe(this, Observer {chart ->
            binding.chartView.setChart(chart)
        })
    }
}
