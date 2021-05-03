package no.kristiania.pgr208_exam.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.databinding.TransactionsItemBinding
import no.kristiania.pgr208_exam.datastorage.entities.UserTransaction
import java.util.*

class TransactionsAdapter(private val userTransactions: List<UserTransaction>) : RecyclerView.Adapter<TransactionsAdapter.TransactionViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val holder = TransactionsItemBinding.inflate(LayoutInflater.from(parent.context))
        return TransactionViewHolder(holder)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(userTransactions[position])
    }

    override fun getItemCount(): Int = userTransactions.size

    class TransactionViewHolder(private val binding : TransactionsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction : UserTransaction) {
            val imageUrl = "https://static.coincap.io/assets/icons/${transaction.symbol.toLowerCase(
                Locale.ROOT)}@2x.png"
            Glide.with(binding.root.context).load(imageUrl).into(binding.transactionSymbol)
            binding.timeOfTransaction.text = transaction.time
            binding.transactionType.text = transaction.type
            binding.volumeForBuyValue.text = "${transaction.volume} ${transaction.symbol} for ${transaction.usdBuyAmount} USD"
        }
    }
}