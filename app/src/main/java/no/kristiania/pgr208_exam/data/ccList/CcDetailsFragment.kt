package no.kristiania.pgr208_exam.data.ccList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.Image


class CcDetailsFragment(val image : Image) : Fragment(R.layout.cc_details_fragment) {
    private lateinit var binding:CcDetailsFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CcDetailsFragment.bind
        Glide.with(this).load(image.urls.regular).into(binding.image)
    }
}