package no.kristiania.pgr208_exam.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import no.kristiania.pgr208_exam.datastorage.entities.PortfolioStatement
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.activities.TransactionsActivity
import no.kristiania.pgr208_exam.adapters.UserPortfolioAdapter
import no.kristiania.pgr208_exam.databinding.UserPortfolioFragmentBinding
import no.kristiania.pgr208_exam.viewmodels.UserPortfolioViewModel

class UserPortfolioFragment : Fragment(R.layout.user_portfolio_fragment) {

    private lateinit var binding: UserPortfolioFragmentBinding

    private var portfolios = mutableListOf<PortfolioStatement>()

    private lateinit var userPortfolioViewModel: UserPortfolioViewModel

    private lateinit var adapter: UserPortfolioAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPortfolioViewModel = UserPortfolioViewModel(requireActivity().application)

        binding = UserPortfolioFragmentBinding.bind(view)

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        adapter = UserPortfolioAdapter(portfolios)

        binding.recyclerView.adapter = adapter

        binding.btnTransactions.setOnClickListener{
            val intent = Intent(requireContext(), TransactionsActivity::class.java)
            startActivity(intent)
        }

        mountObservers()
    }

    private fun mountObservers() {
        userPortfolioViewModel.portfolioStatements.observe(this, Observer { portfolioStatements ->
            portfolios.clear()
            portfolios.addAll(portfolioStatements)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        userPortfolioViewModel.getPortfolioStatements()
    }
}