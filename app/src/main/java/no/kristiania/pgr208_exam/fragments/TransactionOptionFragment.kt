package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionActivity
import no.kristiania.pgr208_exam.data.domain.SpecificCcHistory
import no.kristiania.pgr208_exam.databinding.TransactionOptionFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.OverviewViewModel
import java.util.*
import kotlin.collections.ArrayList

class TransactionOptionFragment(id: String) : Fragment(R.layout.transaction_option_fragment){

    private lateinit var binding : TransactionOptionFragmentBinding

    var ccIntervals = mutableListOf<SpecificCcHistory>()
    val currency: String = id

    // getInterval method should be in anther viewmodel
    private val viewModel: OverviewViewModel = OverviewViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = TransactionOptionFragmentBinding.bind(view)

        binding.buyBtn.setOnClickListener {
            (context as TransactionActivity).supportFragmentManager.beginTransaction().replace(R.id.transactionFragmentContainer, TransactionBuyFragment(), "TransactionBuyFragment").commit()
        }

        binding.sellBtn.setOnClickListener {
            (context as TransactionActivity).supportFragmentManager.beginTransaction().replace(R.id.transactionFragmentContainer, TransactionSellFragment(), "TransactionSellFragment").commit()
        }

        displayGraph()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getInterval(currency)
    }

    fun displayGraph() {
        viewModel.getInterval(currency)
        viewModel.ccHistory.observe(this, Observer {history ->
            Log.d("INFO", "Price usd: ${history.data[0].priceUsd}")
            ccIntervals.clear()
            ccIntervals.addAll(history.data)


            var series: ArrayList<DataEntry> = ArrayList()

            for (ccInterval in ccIntervals) {
                series.add(ValueDataEntry(ccInterval.date, ccInterval.priceUsd?.toFloat()))
            }


            val cartesian: Cartesian = AnyChart.line()

            val xAxis = cartesian.xAxis(0)
            val yAxis = cartesian.yAxis(0)

            xAxis.labels().format("{%Value}{dateTimeFormat:MM-dd HH-mm}")

            //yAxis.title("USD")
            yAxis.labels().format("\${%Value}{scale:(1000)(1000)(1000)|(k)(m)(b)}")

            cartesian.line(series)

            binding.chartView.setChart(cartesian)
        })
    }

}
