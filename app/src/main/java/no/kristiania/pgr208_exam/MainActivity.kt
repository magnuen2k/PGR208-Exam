package no.kristiania.pgr208_exam


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.kristiania.pgr208_exam.data.ccList.CcOverviewFragment
import no.kristiania.pgr208_exam.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.fragment_container, CcOverviewFragment(), "comics_list").commit()
    }
}