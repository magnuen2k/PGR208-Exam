package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionActivity
import no.kristiania.pgr208_exam.databinding.TransactionBuyFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.TransactionViewModel

class TransactionBuyFragment : Fragment(R.layout.transaction_buy_fragment){

    private lateinit var binding : TransactionBuyFragmentBinding

    private lateinit var viewModel: TransactionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider((context as TransactionActivity)).get(TransactionViewModel::class.java)

        binding = TransactionBuyFragmentBinding.bind(view)

        // Better way to pass data to fragment from activity?
        val recentRate = (context as TransactionActivity).getRecentRate()
        val currency = (context as TransactionActivity).getCurrency()
        val symbol = (context as TransactionActivity).getCurrencySymbol()

        val usdBuyAmount = binding.usdBuyAmount.text

        binding.ccName.text = currency

        //binding.ccBuyAmount.text = (usdBuyAmount.toString().toDouble() / recentRate.toDouble()).toString()

        binding.confirmBuyBtn.setOnClickListener {
            // Put input usd / recent rate into database to selected cc
            val recentRateArr = recentRate.split(",")
            val recentRateDouble = (recentRateArr[0] + recentRateArr[1]).toDouble()
            Log.d("INFO", "recentRate: ${recentRateDouble}")
            val volume: String = (usdBuyAmount.toString().toDouble() / recentRateDouble).toString()
            viewModel.insertCc(symbol, volume)
        }
    }
    
    override fun onResume() {
        super.onResume()
    }

}