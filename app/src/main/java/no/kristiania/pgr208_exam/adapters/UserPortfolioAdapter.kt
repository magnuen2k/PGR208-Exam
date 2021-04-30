package no.kristiania.pgr208_exam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.databinding.UserPortfolioStatementBinding
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio
import java.util.*

class UserPortfolioAdapter(private val list: List<UserPortfolio>) : RecyclerView.Adapter<UserPortfolioAdapter.StatementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatementViewHolder {
        val holder = UserPortfolioStatementBinding.inflate(LayoutInflater.from(parent.context))
        return StatementViewHolder(holder)
    }

    override fun onBindViewHolder(holder: StatementViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class StatementViewHolder(private val binding: UserPortfolioStatementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(statement : UserPortfolio) {
            val imageUrl = "https://static.coincap.io/assets/icons/" + statement.symbol?.toLowerCase(
                Locale.ROOT) + "@2x.png"
            Glide.with(binding.root.context).load(imageUrl).into(binding.ccIcon)
            binding.ccVolume.text = statement.volume
        }
    }
}