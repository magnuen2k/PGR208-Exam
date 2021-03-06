package no.kristiania.pgr208_exam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.datastorage.entities.PortfolioStatement
import no.kristiania.pgr208_exam.databinding.UserPortfolioStatementBinding
import no.kristiania.pgr208_exam.utils.formatDecimal
import java.util.*

class UserPortfolioAdapter(private val list: List<PortfolioStatement>) : RecyclerView.Adapter<UserPortfolioAdapter.StatementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatementViewHolder {
        val holder = UserPortfolioStatementBinding.inflate(LayoutInflater.from(parent.context))
        return StatementViewHolder(holder)
    }

    override fun onBindViewHolder(holder: StatementViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class StatementViewHolder(private val binding: UserPortfolioStatementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(statement : PortfolioStatement) {
            val imageUrl = "https://static.coincap.io/assets/icons/${statement.symbol.toLowerCase(
                Locale.ROOT)}@2x.png"
            Glide.with(binding.root.context).load(imageUrl).into(binding.ccIcon)
            if (statement.recentRate.isNullOrBlank() || statement.totalValue.isNullOrBlank()) {
                binding.volAndRate.text = "${statement.volume.formatDecimal()} USD"
            } else {
                binding.volAndRate.text = "${statement.volume.formatDecimal("#,###.00000000")} x ${statement.recentRate.formatDecimal()}"
                binding.totalValue.text = "${statement.totalValue.formatDecimal()} USD"
            }
        }
    }
}