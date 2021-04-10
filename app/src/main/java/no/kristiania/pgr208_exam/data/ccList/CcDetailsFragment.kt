package no.kristiania.pgr208_exam.data.ccList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.databinding.CcDetailsFragmentBinding


class CcDetailsFragment(val ccOverview : CcOverview) : Fragment(R.layout.cc_details_fragment) {
    private lateinit var binding:CcDetailsFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CcDetailsFragmentBinding.bind(view)
        //Glide.with(this).load(ccOverview.urls.regular).into(binding.image)
    }
}