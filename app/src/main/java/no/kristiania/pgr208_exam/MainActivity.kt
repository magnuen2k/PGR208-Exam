package no.kristiania.pgr208_exam


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.data.ccList.CcOverviewFragment
import no.kristiania.pgr208_exam.databinding.ActivityMainBinding
import no.kristiania.pgr208_exam.db.DataBase
import no.kristiania.pgr208_exam.entities.UserPortfolio


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, CcOverviewFragment(), "comics_list").commit()

        sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val launch: String? = sharedPreferences.getString("launch", null)

        if(launch == null) {
            sharedPreferences.edit().putString("launch", "exist").apply()
            lifecycleScope.launch {
                DataBase.getDatabase(baseContext).getUserPortfolioDAO().insert(UserPortfolio("USD", "10000"))
            }
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                DataBase.getDatabase(baseContext).getUserPortfolioDAO().fetchAll().forEach {
                    binding.points.text = it.volume
                }
            }
        }
    }
}