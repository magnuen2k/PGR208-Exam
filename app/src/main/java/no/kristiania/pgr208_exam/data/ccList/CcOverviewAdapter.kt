package no.kristiania.pgr208_exam.data.ccList

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.databinding.CcOverviewItemBinding
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.data.domain.SpecificCcData
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*


class CcOverviewAdapter(private val list: MutableList<SpecificCcData>, val onClick: (CcOverview) -> Unit) : RecyclerView.Adapter<CcOverviewAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val holder = CcOverviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return ImageViewHolder(holder, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class ImageViewHolder(private val binding: CcOverviewItemBinding, val onClick: (CcOverview) -> Unit) : RecyclerView.ViewHolder(binding.root){
         fun bind(ccOverview: SpecificCcData){

             binding.ccId.text = ccOverview.id
             binding.symbol.text = ccOverview.symbol
             binding.currencyUsd.text = formatDecimal(ccOverview.priceUsd)
             binding.changePercent.text = formatDecimal(ccOverview.changePercent24Hr)
             Glide.with(binding.root.context).load("https://static.coincap.io/assets/icons/" + ccOverview.symbol?.toLowerCase(
                 Locale.ROOT) + "@2x.png").into(binding.imgSmaller)
        }

        private fun formatDecimal(decimal: String?) : String {
            val priceUsd = decimal?.toBigDecimal()
            val format = DecimalFormat("#,###.00")
            format.isParseBigDecimal = true
            format.minimumIntegerDigits = 1
            return format.format(priceUsd)
        }

    }

}