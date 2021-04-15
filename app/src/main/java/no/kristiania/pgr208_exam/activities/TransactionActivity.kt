package no.kristiania.pgr208_exam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.data.domain.CcOverview
import no.kristiania.pgr208_exam.databinding.ActivityTransactionBinding
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio
import no.kristiania.pgr208_exam.fragments.TransactionOptionFragment

class TransactionActivity : AppCompatActivity() {

    private lateinit var currencySymbol : String
    private lateinit var currency : String
    private lateinit var recentRate : String
    private lateinit var symbol : String

    private val _userPortfolio = MutableLiveData<UserPortfolio>()
    val userPortfolio: LiveData<UserPortfolio> get() = _userPortfolio

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
            symbol =  extras.getString("symbol", "")
            getPortfolio(symbol)
            binding.currency.text = currency
            binding.recentRate.text = recentRate
            Glide.with(this).load(currencySymbol).into(binding.currencySymbol)
        }
    }

    private fun getPortfolio(symbol: String) {
        lifecycleScope.launch {
            val portfolio = DataBase.getDatabase(baseContext).getUserPortfolioDAO().fetch(symbol)
            Log.d("INFO", "Symbol: ${portfolio.symbol} Volume: ${portfolio.volume} ")
            _userPortfolio.postValue(portfolio)
        }
    }

}