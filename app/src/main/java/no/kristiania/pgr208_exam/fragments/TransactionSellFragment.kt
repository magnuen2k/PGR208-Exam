package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.databinding.TransactionSellFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.TransactionViewModel
import java.util.*

class TransactionSellFragment : Fragment(R.layout.transaction_sell_fragment){

    private lateinit var binding : TransactionSellFragmentBinding

    private lateinit var viewModel: TransactionViewModel

    private lateinit var recentRate: String
    private lateinit var currency: String
    private lateinit var symbol: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)

        binding = TransactionSellFragmentBinding.bind(view)


        recentRate = arguments?.getString("recentRate").toString()
        currency = arguments?.getString("currency").toString()
        symbol = arguments?.getString("symbol").toString()

        val ccSellAmount = binding.ccSellAmount.text

        binding.ccName.text = symbol

        binding.textViewInfoMessage.text = "You can only convert cryptocurrency to USD"



        binding.ccSellAmount.addTextChangedListener(textWatcher)

        var currencyVolume = "0"

        viewModel.userPortfolio.observe(this, androidx.lifecycle.Observer {
            currencyVolume = it.volume
            binding.textViewVolumeOwned.text = "You have ${it.volume} ${it.symbol}"
        })

        viewModel.getPortfolio(symbol)
        var userUsdBalance = ""
        viewModel.userUsd.observe(this, androidx.lifecycle.Observer { portfolio ->
            userUsdBalance = portfolio.volume
        })

        viewModel.getUserUsd()

        binding.confirmBtn.setOnClickListener {
            sellCurrency(recentRate, symbol, ccSellAmount.toString(), currencyVolume, userUsdBalance)
            fragmentManager?.popBackStackImmediate()
        }
    }

    var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
        override fun afterTextChanged(s: Editable?) {
            if(!binding.ccSellAmount.text.isNullOrBlank()) {
                binding.usdAmount.text = (binding.ccSellAmount.text.toString().toDouble() * formatRecentRate(recentRate)).toString()
            } else {
                binding.usdAmount.text = ""
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

    private fun sellCurrency(recentRate: String, symbol: String, currencyVolumeToSell: String, currencyVolumeOwned: String, userUsdBalance: String) {
        if (currencyVolumeOwned.toDouble() < currencyVolumeToSell.toDouble()) {
            Snackbar.make(binding.root, "You have ${currencyVolumeOwned} and wanted to sell ${currencyVolumeToSell}", Snackbar.LENGTH_SHORT).show()
        } else {
            val newVolume = (currencyVolumeOwned.toDouble() - currencyVolumeToSell.toDouble()).toString()
            val volumeSold = currencyVolumeToSell
            val volumeSoldFor = (currencyVolumeToSell.toDouble() * recentRate.toDouble()).toString()
            val currentTime = Calendar.getInstance().time
            val newUsdBalance = (userUsdBalance.toDouble() + volumeSoldFor.toDouble()).toString()
            viewModel.insertPortfolio(symbol, newVolume, volumeSold, volumeSoldFor, currentTime.toString(), "SOLD")
            viewModel.updateUsd(newUsdBalance)
        }
    }

}