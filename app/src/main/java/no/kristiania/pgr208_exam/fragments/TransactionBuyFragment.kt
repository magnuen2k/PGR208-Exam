package no.kristiania.pgr208_exam.fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.databinding.TransactionBuyFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.TransactionViewModel
import java.util.*

class TransactionBuyFragment : Fragment(R.layout.transaction_buy_fragment){

    private lateinit var binding : TransactionBuyFragmentBinding

    private lateinit var viewModel: TransactionViewModel

    private lateinit var recentRate: String
    private lateinit var currency: String
    private lateinit var symbol: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(TransactionViewModel::class.java)

        binding = TransactionBuyFragmentBinding.bind(view)

        recentRate = arguments?.getString("recentRate").toString()
        currency = arguments?.getString("currency").toString()
        symbol = arguments?.getString("symbol").toString()


        val usdBuyAmount = binding.usdBuyAmount.text

        binding.ccName.text = symbol

        binding.textViewInfoMessage.text = "You can only buy cryptocurrency with USD"

        binding.usdBuyAmount.addTextChangedListener(textWatcher)

        var userUsd = "0"
        var prevVolume = ""
        viewModel.userUsd.observe(this, Observer {portfolio ->
            userUsd = portfolio.volume
            binding.textViewVolumeOwned.text = "You have ${portfolio.volume} USD"
        })

        viewModel.userPortfolio.observe(this, Observer {portfolio ->
            prevVolume = portfolio.volume
        })

        viewModel.getPortfolio(symbol)


        viewModel.getUserUsd()

        binding.confirmBtn.setOnClickListener {
            buyCurrency(recentRate, symbol, usdBuyAmount.toString(), userUsd, prevVolume)
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


    private fun buyCurrency(recentRate: String, symbol: String, usdBuyAmount: String, userUsd: String, prevVolume : String) {
        when {
            userUsd.toDouble() < usdBuyAmount.toDouble() -> {
                Snackbar.make(
                        binding.root,
                        "You don't have enough money",
                        Snackbar.LENGTH_SHORT
                ).show()
            }
            else -> {

                // Calculate new volume
                val ccVolume: String = (usdBuyAmount.toDouble() / recentRate.toDouble()).toString()
                val newVolume: String = (ccVolume.toDouble() + prevVolume.toDouble()).toString()

                val currentTime = Calendar.getInstance().time

                // Insert cc and remove usd in DB
                viewModel.insertPortfolio(symbol, newVolume, ccVolume, usdBuyAmount, currentTime.toString(), "BOUGHT")
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
}