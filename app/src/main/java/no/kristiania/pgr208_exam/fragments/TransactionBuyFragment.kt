package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
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

        // Need to know if user has enough USD to buy
        var userUsd = "0"
        viewModel.getUserUsd()
        viewModel.userUsd.observe(this, Observer {portfolio ->
            userUsd = portfolio.volume
        })

        binding.confirmBtn.setOnClickListener {
           buyCurrency(recentRate, symbol, usdBuyAmount.toString(), userUsd)
        }
    }

    private fun buyCurrency(recentRate: String, symbol: String, usdBuyAmount: String, userUsd: String) {
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

        when {
            userUsd.toDouble() < usdBuyAmount.toDouble() -> {
                Snackbar.make(
                        binding.root,
                        "You don't have enough money",
                        Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> {
                // Get old volume and add it to new
                var prevVolume = ""
                viewModel.getPortfolio(symbol)
                viewModel.userPortfolio.observe(this, Observer {portfolio ->
                    prevVolume = portfolio.volume
                })

                // Calculate new volume
                val volume: String = ((usdBuyAmount.toDouble() / recentRateDouble) + prevVolume.toDouble()).toString()

                // Maybe have both of these transactions happen in same viewmodel function instead?
                // Insert cc and remove usd in DB
                viewModel.insertPortfolio(symbol, volume)
                viewModel.insertPortfolio("USD", (userUsd.toDouble() - usdBuyAmount.toDouble()).toString())

                // TODO Important to redirect here (or check a users balance for each click, if not a user can spam the buy button
            }
        }
    }


    private fun getUserUsd() {
    }

    private fun getPrevVolume(symbol: String) {
    }
}