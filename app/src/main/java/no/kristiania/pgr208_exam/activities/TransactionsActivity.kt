package no.kristiania.pgr208_exam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.adapters.TransactionsAdapter
import no.kristiania.pgr208_exam.databinding.ActivityTransactionsBinding
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserTransaction

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionsBinding

    private var transactions = mutableListOf<UserTransaction>()

    private lateinit var adapter: TransactionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TransactionsAdapter(transactions)

        binding.recyclerView.adapter = adapter

       binding.recyclerView.layoutManager = GridLayoutManager(this, 1)


        lifecycleScope.launch(Dispatchers.IO) {
            transactions.clear()

            val userTransactions = DataBase.getDatabase(applicationContext).getUserTransactionsDAO().fetchAll()
            transactions.addAll(userTransactions)

            adapter.notifyDataSetChanged()

        }



    }
}