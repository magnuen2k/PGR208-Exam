package no.kristiania.pgr208_exam


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import no.kristiania.pgr208_exam.data.ccList.CcOverviewFragment
import no.kristiania.pgr208_exam.databinding.ActivityMainBinding


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
            Log.d("MAIN_ACTIVITY", "FIRST LAUNCH")
            sharedPreferences.edit().apply() {
                putString("launch", "exist")
            }
        } else {
            Log.d("MAIN_ACTIVITY", "NOT FIRST LAUNCH")
        }
    }
}