package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionActivity
import no.kristiania.pgr208_exam.databinding.TransactionSellFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.TransactionViewModel

class TransactionSellFragment : Fragment(R.layout.transaction_sell_fragment){

    private lateinit var binding : TransactionSellFragmentBinding

    private lateinit var viewModel: TransactionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider((context as TransactionActivity)).get(TransactionViewModel::class.java)

        binding = TransactionSellFragmentBinding.bind(view)

        // Better way to pass data to fragment from activity?
        val recentRate = arguments?.getString("recentRate")
        val currency = arguments?.getString("currency")
        val symbol = arguments?.getString("symbol")

        val ccSellAmount = binding.ccSellAmount.text

        binding.confirmBtn.setOnClickListener {
            Log.d("INFO", "Implement sell logic..")
        }
    }

}