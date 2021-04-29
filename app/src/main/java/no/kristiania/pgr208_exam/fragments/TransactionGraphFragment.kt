package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.SpecificCcHistory
import no.kristiania.pgr208_exam.databinding.TransactionGraphFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.TransactionViewModel

class TransactionGraphFragment : Fragment(R.layout.transaction_graph_fragment) {

    private lateinit var viewModel: TransactionViewModel

    private lateinit var binding : TransactionGraphFragmentBinding

    var ccIntervals = mutableListOf<SpecificCcHistory>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        // GRAPH

        viewModel.getInterval()
        viewModel.ccHistory.observe(this, Observer {history ->
            Log.d("INFO", "Price usd: ${history.data[0].priceUsd}")
            ccIntervals.addAll(history.data);
        })

        var series: ArrayList<DataEntry> = ArrayList();

        for (ccInterval in ccIntervals) {
            series.add(ValueDataEntry(ccInterval.time, ccInterval.priceUsd?.toFloat()))
        }

        val cartesian: Cartesian = AnyChart.column()

        binding.chartView.setChart(cartesian)

        // STOP GRAPH

    }
}