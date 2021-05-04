package no.kristiania.pgr208_exam.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.databinding.ActivityHomeBinding
import no.kristiania.pgr208_exam.databinding.ActivityMainBinding
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio
import no.kristiania.pgr208_exam.fragments.CcOverviewFragment
import no.kristiania.pgr208_exam.fragments.UserPortfolioFragment
import no.kristiania.pgr208_exam.viewmodels.OverviewViewModel

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    private var viewModel = OverviewViewModel()

    private var _points = MutableLiveData<String>()
    private var points: LiveData<String> = _points

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, CcOverviewFragment(), "CcOverviewFragment").commit()

        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val launch: String? = sharedPreferences.getString("launch", null)

        if (launch == null) {
            sharedPreferences.edit().putString("launch", "exist").apply()
            lifecycleScope.launch {
                DataBase.getDatabase(baseContext).getUserPortfolioDAO().insert(
                    UserPortfolio(
                        "USD",
                        "10000"
                    )
                )
            }
        }

        // Just to see that inserting to database works properly
        // Should calculate users points
        viewModel.allCcAssets.observe(this, Observer { currencies ->
            lifecycleScope.launch(Dispatchers.IO) {
                val portfolios = DataBase.getDatabase(baseContext).getUserPortfolioDAO().fetchAll()
                var points: Double = 0.0

                val usdPortfolio = portfolios.find { pO -> pO.symbol.equals("USD")}
                usdPortfolio?.let { pO ->
                    points += pO.volume.toDouble()
                }


                portfolios.forEach { portfolio ->
                    val currency =
                        currencies.data.find { ccO -> ccO.symbol.equals(portfolio.symbol) }
                    currency?.priceUsd?.let { priceUsd ->
                        points += priceUsd.toDouble() * portfolio.volume.toDouble()
                    }
                }

                _points.postValue(points.toString())
            }
        })

        points.observe(this, Observer { newPoints ->
            binding.points.text = newPoints
        })

        binding.pointsHeader.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, UserPortfolioFragment(), "UserPortfolioFragment")
                .addToBackStack("").commit()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("INFO", "[HomeActivity.kt] onResume ran")
        viewModel.getAssetOverview()

    }
}