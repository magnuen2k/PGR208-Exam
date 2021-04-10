package no.kristiania.pgr208_exam.data.ccList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.pgr208_exam.databinding.CcOverviewItemBinding
import no.kristiania.pgr208_exam.data.domain.Image


class CcOverviewAdapter(private val list: List<Image>, val onClick : (Image) -> Unit) : RecyclerView.Adapter<CcOverviewAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val holder = CcOverviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return ImageViewHolder(holder, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class ImageViewHolder(private val binding: CcOverviewItemBinding, val onClick: (Image) -> Unit) : RecyclerView.ViewHolder(binding.root){

         fun bind(image: Image){
        }

    }

}