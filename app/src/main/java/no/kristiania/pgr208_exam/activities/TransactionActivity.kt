package no.kristiania.pgr208_exam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.kristiania.pgr208_exam.R

class TransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        val extras = intent.extras
        if (extras !== null) {

        }
    }
}