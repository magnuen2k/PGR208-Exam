package no.kristiania.pgr208_exam.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import no.kristiania.pgr208_exam.viewmodels.OverviewViewModel
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.databinding.CcFragmentListBinding
import com.google.android.material.snackbar.Snackbar
import no.kristiania.pgr208_exam.adapters.CcOverviewAdapter
import no.kristiania.pgr208_exam.data.domain.SpecificCcData


class CcOverviewFragment : Fragment(R.layout.cc_fragment_list) {

    private lateinit var binding: CcFragmentListBinding

    var ccOverviews = mutableListOf<SpecificCcData>()

    private val viewModel: OverviewViewModel = OverviewViewModel()

    private lateinit var adapter: CcOverviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = CcFragmentListBinding.bind(view)
        adapter = CcOverviewAdapter(ccOverviews) { item ->
            showDetails(item)
        }

        mountObservers()

        with(binding) {
            ccList.layoutManager = GridLayoutManager(requireContext(), 1)
            ccList.adapter = adapter
        }
    }

    private fun mountObservers() {
        viewModel.error.observe(this, Observer {
            Snackbar.make(
                    binding.root,
                    "Failed to fetch images. Do you have an internet connection?",
                    Snackbar.LENGTH_INDEFINITE
            ).setAction("Retry") { viewModel.getAssetOverview()}.show()
        })


        viewModel.allCcAssets.observe(this, Observer { newCcData ->
            ccOverviews.clear()
            ccOverviews.addAll(newCcData.data)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAssetOverview()
    }

    private fun showDetails(ccOverview: CcOverview) {

        fragmentManager?.apply {
            beginTransaction()
                .replace(R.id.fragment_container, CcDetailsFragment(ccOverview))
                .addToBackStack("image_post_fragment").commit()
        }

        Log.d("INFO", "Image clicked")
    }
}