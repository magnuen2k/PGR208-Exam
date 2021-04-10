package no.kristiania.pgr208_exam.data.imageList
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.Image
import no.kristiania.pgr208_exam.databinding.ImagePostFragmentBinding


class ImagePostFragment(val image : Image) : Fragment(R.layout.image_post_fragment) {
    private lateinit var binding:ImagePostFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ImagePostFragmentBinding.bind(view)
        Glide.with(this).load(image.urls.regular).into(binding.image)
    }

}