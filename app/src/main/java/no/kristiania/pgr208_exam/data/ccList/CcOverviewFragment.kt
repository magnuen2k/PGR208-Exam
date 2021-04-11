package no.kristiania.pgr208_exam.data.ccList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.pgr208_exam.MainViewModel
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.databinding.CcFragmentListBinding
import com.google.android.material.snackbar.Snackbar
import no.kristiania.pgr208_exam.data.domain.SpecificCcData

class CcOverviewFragment : Fragment(R.layout.cc_fragment_list) {

    private lateinit var binding: CcFragmentListBinding

    var ccOverviews = mutableListOf<SpecificCcData>()

    private val viewModel: MainViewModel = MainViewModel()

    private lateinit var adapter: CcOverviewAdapter

    private lateinit var layoutManager: GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = CcFragmentListBinding.bind(view)
        adapter = CcOverviewAdapter(ccOverviews) { image ->
            showImageDetails(image)
        }

        viewModel.error.observe(this, Observer {
            Snackbar.make(
                binding.root,
                "Failed to fetch images. Do you have an internet connection?",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Retry") { viewModel.reload() }.show()
        })

        with(binding) {
            layoutManager = GridLayoutManager(requireContext(), 1)
            ccList.layoutManager = layoutManager
            ccList.adapter = adapter
        }

        viewModel.allCcAssets.observe(this, Observer { newCcData ->
            ccOverviews.addAll(newCcData.data)
            adapter.notifyDataSetChanged()
        })
    }

    private fun showImageDetails(ccOverview: CcOverview) {

        fragmentManager?.apply {
            beginTransaction()
                .replace(R.id.fragment_container, CcDetailsFragment(ccOverview))
                .addToBackStack("image_post_fragment").commit()
        }

        Log.d("INFO", "Image clicked")
    }
}