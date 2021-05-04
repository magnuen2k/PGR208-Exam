package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionActivity
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

        viewModel = ViewModelProvider((context as TransactionActivity)).get(TransactionViewModel::class.java)

        binding = TransactionSellFragmentBinding.bind(view)

        // Better way to pass data to fragment from activity?
        recentRate = arguments?.getString("recentRate").toString()
        currency = arguments?.getString("currency").toString()
        symbol = arguments?.getString("symbol").toString()

        val ccSellAmount = binding.ccSellAmount.text

        binding.ccName.text = symbol



        binding.ccSellAmount.addTextChangedListener(textWatcher)

        binding.confirmBtn.setOnClickListener {
            Log.d("INFO", "Implement sell logic..")
        }
    }

    var textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val currentTime = Calendar.getInstance().time
            Log.d("INFO", "CurrentTime is: $currentTime")
        }
        override fun afterTextChanged(s: Editable?) {
            // Need to trim decimals
            if(!binding.ccSellAmount.text.isNullOrBlank()) {
                //val rate = recentRate.replace("\\s".toRegex(), "")
                //Log.d("INFO", "Recent rate: ${rate}")
                binding.usdAmount.text = (binding.ccSellAmount.text.toString().toDouble() / formatRecentRate(recentRate)).toString()
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

}