package no.kristiania.pgr208_exam.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionActivity
import no.kristiania.pgr208_exam.databinding.TransactionBuyFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.TransactionViewModel
import java.time.LocalDateTime
import java.util.*

class TransactionBuyFragment : Fragment(R.layout.transaction_buy_fragment){

    private lateinit var binding : TransactionBuyFragmentBinding

    private lateinit var viewModel: TransactionViewModel

    private lateinit var recentRate: String
    private lateinit var currency: String
    private lateinit var symbol: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider((context as TransactionActivity)).get(TransactionViewModel::class.java)

        binding = TransactionBuyFragmentBinding.bind(view)

        // Better way to pass data to fragment from activity?

        recentRate = arguments?.getString("recentRate").toString()
        currency = arguments?.getString("currency").toString()
        symbol = arguments?.getString("symbol").toString()


        val usdBuyAmount = binding.usdBuyAmount.text

        binding.ccName.text = symbol

        // Update how much bitcoin what you type in USD

        //binding.ccBuyAmount.text = (usdBuyAmount.toString().toDouble() / recentRate.toDouble()).toString()
        binding.usdBuyAmount.addTextChangedListener(textWatcher)

        // Need to know if user has enough USD to buy
        var userUsd = "0"
        viewModel.getUserUsd()
        viewModel.userUsd.observe(this, Observer {portfolio ->
            userUsd = portfolio.volume
        })

        binding.confirmBtn.setOnClickListener {
            buyCurrency(recentRate, symbol, usdBuyAmount.toString(), userUsd)
            fragmentManager?.popBackStackImmediate()
        }
    }


    var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            // Need to trim decimals
            if(!binding.usdBuyAmount.text.isNullOrBlank()) {
                binding.ccBuyAmount.text = (binding.usdBuyAmount.text.toString().toDouble() / formatRecentRate(recentRate)).toString()
            } else {
                binding.ccBuyAmount.text = ""
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buyCurrency(recentRate: String, symbol: String, usdBuyAmount: String, userUsd: String) {
        // If recent rate contains a "," we need to remove it to be able to cast to double
        val recentRateDouble = formatRecentRate(recentRate)

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
                val ccVolume: String = (usdBuyAmount.toDouble() / recentRateDouble).toString()
                val newVolume: String = (ccVolume.toDouble() + prevVolume.toDouble()).toString()

                val currentTime = LocalDateTime.now()

                // Insert cc and remove usd in DB
                viewModel.insertPortfolio(symbol, newVolume, ccVolume, usdBuyAmount, currentTime.toString(), "BUY")
                viewModel.updateUsd((userUsd.toDouble() - usdBuyAmount.toDouble()).toString())
            }
        }
    }

    private fun formatRecentRate(recentRate: String): Double {
        return when {
            recentRate.contains(",") -> {
                val recentRateArr = recentRate.split(",")
                (recentRateArr[0] + recentRateArr[1]).toDouble()
            }
            else -> {
                recentRate.toDouble()
            }
        }
    }


    private fun getUserUsd() {
    }

    private fun getPrevVolume(symbol: String) {
    }
}