package com.test.funfactsapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.viewmodel.FunFactsViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var downloadButton: Button = findViewById(R.id.download_button)
        var saveButton: Button = findViewById(R.id.save_Button)
        var displayText: TextView = findViewById(R.id.display_text)

        val funFactsViewModel = ViewModelProvider(this)[FunFactsViewModel::class.java]

        launch(Dispatchers.IO) {
            val defaultText = funFactsViewModel.getFact(false).toString()
            withContext(Dispatchers.Main) {
                displayText.text = defaultText
            }
        }

        downloadButton.setOnClickListener {
            launch(Dispatchers.Main) {
                displayText.text = funFactsViewModel.getFact(true) ?: "Error downloading fact"
            }
        }

        saveButton.setOnClickListener {
            launch(Dispatchers.IO) {
                val saveFact = FunFact((0..1000).random(), displayText.text.toString())
                funFactsViewModel.saveFact(saveFact)
            }
        }
    }
}