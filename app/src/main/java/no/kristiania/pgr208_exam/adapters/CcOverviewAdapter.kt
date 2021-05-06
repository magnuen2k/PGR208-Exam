package no.kristiania.pgr208_exam.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.databinding.CcOverviewItemBinding
import no.kristiania.pgr208_exam.data.domain.SpecificCcData
import no.kristiania.pgr208_exam.utils.formatDecimal
import java.text.DecimalFormat
import java.util.*


class CcOverviewAdapter(private val list: MutableList<SpecificCcData>, val onClick: (SpecificCcData) -> Unit) : RecyclerView.Adapter<CcOverviewAdapter.CcOverviewItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CcOverviewItemHolder {

        val holder = CcOverviewItemBinding.inflate(LayoutInflater.from(parent.context))
        return CcOverviewItemHolder(holder, onClick)
    }

    override fun onBindViewHolder(holder: CcOverviewItemHolder, position: Int) {

        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class CcOverviewItemHolder(private val binding: CcOverviewItemBinding, val onClick: (SpecificCcData) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ccOverview: SpecificCcData) {

            binding.ccId.text = ccOverview.id?.capitalize()
            binding.symbol.text = ccOverview.symbol
            binding.currencyUsd.text = "$${ccOverview.priceUsd.formatDecimal()}"
            binding.changePercent.text = "${formatPercent(ccOverview.changePercent24Hr, binding.changePercent)}%"
            val imageUrl = "https://static.coincap.io/assets/icons/" + ccOverview.symbol?.toLowerCase(
                Locale.ROOT) + "@2x.png"
            Glide.with(binding.root.context).load(imageUrl).into(binding.imgSmaller)

            binding.overviewRoot.setOnClickListener { onClick(ccOverview) }
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
            return decimal.formatDecimal()
        }
    }

}