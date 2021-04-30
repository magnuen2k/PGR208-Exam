package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

        // Update how much bitcoin what you type in USD
        //binding.ccBuyAmount.text = (usdBuyAmount.toString().toDouble() / recentRate.toDouble()).toString()

        binding.confirmBuyBtn.setOnClickListener {
            // Put input usd / recent rate into database to selected cc
            // Need to know if user has enough USD to buy

            // If recent rate contains a "," we need to remove it to be able to cast to double
            val recentRateDouble = when {
                recentRate.contains(",") -> {
                    val recentRateArr = recentRate.split(",")
                    (recentRateArr[0] + recentRateArr[1]).toDouble()
                }
                else -> {
                    recentRate.toDouble()
                }
            }


            // Get old value and add it to new
            var prevVolume = ""

            viewModel.getPortfolio(symbol)
            viewModel.userPortfolio.observe(this, Observer {portfolio ->
                prevVolume = portfolio.volume
            })

            val volume: String = ((usdBuyAmount.toString().toDouble() / recentRateDouble) + prevVolume.toDouble()).toString()

            // Insert into DB
            viewModel.insertCc(symbol, volume)
        }
    }
    
    override fun onResume() {
        super.onResume()
    }

}