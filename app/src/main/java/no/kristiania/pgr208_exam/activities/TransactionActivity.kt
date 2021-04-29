package no.kristiania.pgr208_exam.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.bumptech.glide.Glide
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.SpecificCcHistory
import no.kristiania.pgr208_exam.databinding.ActivityTransactionBinding
import no.kristiania.pgr208_exam.fragments.TransactionOptionFragment
import no.kristiania.pgr208_exam.viewmodels.TransactionViewModel
import java.util.*
import kotlin.collections.ArrayList

class TransactionActivity : AppCompatActivity() {

    private lateinit var currencySymbol : String
    private lateinit var currency : String
    private lateinit var recentRate : String
    private lateinit var symbol : String

    private lateinit var viewModel: TransactionViewModel

    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras !== null) {
            currencySymbol = extras.getString("currencySymbol", "")
            currency = extras.getString("currency", "")
            recentRate =  extras.getString("recentRate", "")
            symbol =  extras.getString("symbol", "")

            supportFragmentManager.beginTransaction().add(R.id.transactionFragmentContainer, TransactionOptionFragment(currency.toLowerCase(Locale.ROOT)), "TransactionOptionFragment").commit()

            viewModel.userPortfolio.observe(this, Observer {userPortfolio ->
                Log.d("INFO", "Symbol:${userPortfolio.symbol} Volume: ${userPortfolio.volume} ")
            })

            viewModel.getPortfolio(symbol)
            binding.currency.text = currency
            binding.recentRate.text = "$${recentRate}"
            Glide.with(this).load(currencySymbol).into(binding.currencySymbol)
        }
    }
}