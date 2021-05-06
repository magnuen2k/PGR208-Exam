package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionActivity
import no.kristiania.pgr208_exam.data.domain.SpecificCcHistory
import no.kristiania.pgr208_exam.databinding.TransactionOptionFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.OverviewViewModel
import no.kristiania.pgr208_exam.viewmodels.TransactionOptionViewModel
import java.util.*
import kotlin.collections.ArrayList

class TransactionOptionFragment() : Fragment(R.layout.transaction_option_fragment) {

    private lateinit var binding: TransactionOptionFragmentBinding
    private lateinit var viewModel: TransactionOptionViewModel

    var ccIntervals = mutableListOf<SpecificCcHistory>()
    lateinit var currency: String
    lateinit var symbol: String
    lateinit var recentRate: String


    // getInterval method should be in anther viewmodel
    //private val viewModel: OverviewViewModel = OverviewViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = TransactionOptionFragmentBinding.bind(view)

        viewModel = ViewModelProvider(requireActivity()).get(TransactionOptionViewModel::class.java)
        Log.d("INFO", "[TransactionOptionFragment.kt] onViewCreated ran")

        val bundle = arguments
        bundle?.let {
            symbol = bundle.getString("symbol")!!
            currency = bundle.getString("currency")!!.toLowerCase(Locale.ROOT)
            recentRate = bundle.getString("recentRate")!!
        }

        viewModel.currency.observe(this, Observer { data ->
            symbol = data.symbol ?: symbol
            currency = data.id ?: currency
            recentRate = data.priceUsd ?: recentRate
            viewModel.getPortfolio(symbol)
            displayGraph()
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

        binding.buyBtn.setOnClickListener {
            (context as TransactionActivity).supportFragmentManager.beginTransaction().replace(
                R.id.transactionFragmentContainer,
                TransactionBuyFragment().apply { arguments = bundle },
                "TransactionBuyFragment"
            ).addToBackStack("buy").commit()
        }

        binding.sellBtn.setOnClickListener {
            (context as TransactionActivity).supportFragmentManager.beginTransaction().replace(
                R.id.transactionFragmentContainer,
                TransactionSellFragment().apply { arguments = bundle },
                "TransactionSellFragment"
            ).addToBackStack("sell").commit()
        }
        //viewModel.getCurrency(currency)
    }

    override fun onResume() {
        super.onResume()
        Log.d("INFO", "[TransactionOptionFragment.kt] onResume ran")
        viewModel.getUserUsd()
        viewModel.getCurrency(currency)
    }

    fun displayGraph() {
        viewModel.getInterval(currency)
        viewModel.ccHistory.observe(this, Observer { history ->
            ccIntervals.clear()
            ccIntervals.addAll(history.data)


            var series: ArrayList<DataEntry> = ArrayList()

            for (ccInterval in ccIntervals) {
                series.add(ValueDataEntry(ccInterval.date, ccInterval.priceUsd?.toFloat()))
            }

            val cartesian: Cartesian = AnyChart.line()

            val xAxis = cartesian.xAxis(0)
            val yAxis = cartesian.yAxis(0)
            val tooltip = cartesian.tooltip();

            xAxis.labels().format("{%Value}{dateTimeFormat:MM-dd}")

            yAxis.labels().format("\${%Value}{scale:(1000)(1000)|(k)(m)}")

            tooltip.titleFormat("{%x}{dateTimeFormat:EEEE MMMM dd yyyy HH:mm:ss}")
            tooltip.format("Price: \${%value}{decimalsCount:6}")

            cartesian.line(series)

            binding.chartView.setChart(cartesian)
        })
    }
}
