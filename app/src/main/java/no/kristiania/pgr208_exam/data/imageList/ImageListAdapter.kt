package no.kristiania.pgr208_exam.data.imageList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.databinding.ImageItemBinding
import no.kristiania.pgr208_exam.data.domain.Image


class ImageListAdapter(private val list: List<Image>, val onClick : (Image) -> Unit) : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val holder = ImageItemBinding.inflate(LayoutInflater.from(parent.context))
        return ImageViewHolder(holder, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class ImageViewHolder(private val binding: ImageItemBinding, val onClick: (Image) -> Unit) : RecyclerView.ViewHolder(binding.root){

         fun bind(image: Image){
        }

    }

}