package com.test.funfactsapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.viewmodel.FunFactsViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var downloadButton: Button = findViewById(R.id.download_button)
        var saveButton: Button = findViewById(R.id.save_Button)
        var showAll: Button = findViewById(R.id.display_all)
        var displayText: TextView = findViewById(R.id.display_text)
        var lv: ListView = findViewById(R.id.fact_list);


        val funFactsViewModel = ViewModelProvider(this)[FunFactsViewModel::class.java]


        lifecycleScope.launch(Dispatchers.IO) {
            funFactsViewModel.getFact(false).collect {
                withContext(Dispatchers.Main) {
                    displayText.text = it.data?.text ?: "fact not found"
                }
            }
        }

        downloadButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                funFactsViewModel.getFact(true).collect {
                    withContext(Dispatchers.Main) {
                        displayText.text = it.data?.text ?: "Error downloading fact"
                    }
                }
            }
        }

        saveButton.setOnClickListener {
            val saveFact = FunFact((0..1000).random(), displayText.text.toString())
            funFactsViewModel.saveFact(saveFact)
        }

        showAll.setOnClickListener {
            var currentButtonText: String = showAll.text as String
            if (currentButtonText == getString(R.string.show_all_facts)) {
                displayText.visibility = View.GONE
                showAll.text = getString(R.string.hide_all_facts)
                saveButton.visibility = View.GONE
                downloadButton.visibility = View.GONE
                lv.visibility = View.VISIBLE
            } else {
                displayText.visibility = View.VISIBLE
                showAll.text = getString(R.string.show_all_facts)
                saveButton.visibility = View.VISIBLE
                downloadButton.visibility = View.VISIBLE
                lv.visibility = View.GONE
            }
        }

        launch() {
            val allFactsList: List<FunFact>? = funFactsViewModel.factsState.collect {
            }
        }
    }

}