package no.kristiania.pgr208_exam.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.R
import no.kristiania.pgr208_exam.databinding.ActivityHomeBinding
import no.kristiania.pgr208_exam.databinding.ActivityMainBinding
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio
import no.kristiania.pgr208_exam.fragments.CcOverviewFragment

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, CcOverviewFragment(), "comics_list").commit()


        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val launch: String? = sharedPreferences.getString("launch", null)

        if(launch == null) {
            sharedPreferences.edit().putString("launch", "exist").apply()
            lifecycleScope.launch {
                DataBase.getDatabase(baseContext).getUserPortfolioDAO().insert(
                    UserPortfolio(
                        "USD",
                        "10000"
                    )
                )
            }
        } else {

            // Just to see that inserting to database works properly
            // Should calculate users points
            lifecycleScope.launch(Dispatchers.IO) {
                val portfolios = DataBase.getDatabase(baseContext).getUserPortfolioDAO().fetchAll()

                var points: String = ""

                for(portfolio in portfolios) {
                    points += portfolio.symbol + portfolio.volume
                }

                binding.points.text = points
            }
        }
    }
}