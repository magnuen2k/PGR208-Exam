package no.kristiania.pgr208_exam


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.kristiania.pgr208_exam.activities.HomeActivity
import no.kristiania.pgr208_exam.fragments.CcOverviewFragment
import no.kristiania.pgr208_exam.databinding.ActivityMainBinding
import no.kristiania.pgr208_exam.datastorage.db.DataBase
import no.kristiania.pgr208_exam.datastorage.entities.UserPortfolio


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }, 2000)
    }
}