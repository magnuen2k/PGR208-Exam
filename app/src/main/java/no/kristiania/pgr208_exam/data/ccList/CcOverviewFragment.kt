package no.kristiania.pgr208_exam.data.ccList

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.pgr208_exam.MainViewModel
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.Image
import no.kristiania.pgr208_exam.databinding.ImageFragmentListBinding
import com.google.android.material.snackbar.Snackbar

class CcOverviewFragment : Fragment(R.layout.cc_fragment_list) {

    private lateinit var binding: ImageFragmentListBinding

    var images = mutableListOf<Image>()

    private val viewModel : MainViewModel = MainViewModel()

    private lateinit var adapter: CcOverviewAdapter

    private lateinit var layoutManager : GridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = ImageFragmentListBinding.bind(view)
        adapter = CcOverviewAdapter(images) { image ->
            showImageDetails(image)
        }

        viewModel.error.observe(this) {
            Snackbar.make(
                binding.root,
                "Failed to fetch images. Do you have an internet connection?",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Retry") { viewModel.reload() }.show()
        }

        with(binding) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            comicsList.layoutManager = layoutManager
            comicsList.adapter = adapter
            comicsList.addOnScrollListener(scrollListener)
        }

        viewModel.currentImage.observe(this, Observer { newImages ->
                images.addAll(newImages)
                adapter.notifyDataSetChanged()
        })
    }

    //
    private fun showImageDetails(image: Image) {

        fragmentManager?.apply {
            beginTransaction()
                .replace(R.id.fragment_container, CcDetailsFragment(image))
                .addToBackStack("image_post_fragment").commit()
        }

        Log.d("INFO", "Image clicked")
    }


    private val scrollListener = object : RecyclerView.OnScrollListener() { // Taken from https://gist.github.com/ssinss/e06f12ef66c51252563e
        // TODO look into refactoring this onScrollListener
        private var loading = true
        private val visibleThreshold = 5
        private var previousItemTotal = 0

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val amountOfVisibleItems = recyclerView.childCount
            val totalAmountOfItems = layoutManager.itemCount
            val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()


            if (loading) {
                if (totalAmountOfItems > previousItemTotal) {
                    loading = false;
                    previousItemTotal = totalAmountOfItems;
                }
            }
            if (!loading && (totalAmountOfItems - amountOfVisibleItems)
                <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached

                // Do something
                viewModel.getPaginationPage()

                loading = true;
            }



        }
    }
}