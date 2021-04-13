package no.kristiania.pgr208_exam.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.databinding.CcOverviewItemBinding
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.data.domain.SpecificCcData
import java.text.DecimalFormat
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

    class ImageViewHolder(private val binding: CcOverviewItemBinding, val onClick: (CcOverview) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ccOverview: SpecificCcData) {

            binding.ccId.text = ccOverview.id?.capitalize()
            binding.symbol.text = ccOverview.symbol
            binding.currencyUsd.text = "$${formatDecimal(ccOverview.priceUsd)}"
            binding.changePercent.text = "${formatPercent(ccOverview.changePercent24Hr, binding.changePercent)}%"
            Glide.with(binding.root.context).load("https://static.coincap.io/assets/icons/" + ccOverview.symbol?.toLowerCase(
                    Locale.ROOT) + "@2x.png").into(binding.imgSmaller)
        }

        private fun formatPercent(decimal: String?, textView: TextView): String {
            val num = decimal?.toBigDecimal()?.signum()
            if (num !== null) {
                val color: Int =
                        when {
                            num > 0 -> R.color.positivePercent
                            num < 0 -> R.color.negativePercent
                            else -> R.color.neutralPercent
                        }
                textView.setTextColor(getColor(textView.context, color))
            }
            return formatDecimal(decimal)
        }

        private fun formatDecimal(decimal: String?): String {
            val priceUsd = decimal?.toBigDecimal()
            val format = DecimalFormat("#,###.00")
            format.isParseBigDecimal = true
            format.minimumIntegerDigits = 1
            return format.format(priceUsd)
        }
    }

}