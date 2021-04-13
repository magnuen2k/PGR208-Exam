package no.kristiania.pgr208_exam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.coroutines.coroutineScope
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.databinding.ActivityTransactionBinding
import no.kristiania.pgr208_exam.fragments.TransactionOptionFragment

class TransactionActivity : AppCompatActivity() {

    private lateinit var currencySymbol : String
    private lateinit var currency : String
    private lateinit var recentRate : String

    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.transactionFragmentContainer, TransactionOptionFragment(), "TransactionOptionFragment").commit()

        val extras = intent.extras
        if (extras !== null) {
            currencySymbol = extras.getString("currencySymbol", "")
            currency = extras.getString("currency", "")
            recentRate =  extras.getString("recentRate", "")

            binding.currency.text = currency
            binding.recentRate.text = recentRate
            Glide.with(this).load(currencySymbol).into(binding.currencySymbol)
        }
    }
}