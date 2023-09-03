package com.test.funfactsapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.funfactsapp.adapters.FunFactAdapter
import com.test.funfactsapp.data.FunFactRepository
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.viewmodel.FunFactsViewModel
import com.test.funfactsapp.viewmodel.FunFactsViewModelFactory
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var factAdapter: FunFactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var downloadButton: Button = findViewById(R.id.download_button)
        var saveButton: Button = findViewById(R.id.save_Button)
        var showAll: Button = findViewById(R.id.display_all)
        var displayText: TextView = findViewById(R.id.display_text)
        var factRv: RecyclerView = findViewById(R.id.rv_design);
        var searchView: SearchView = findViewById(R.id.search_view);
        searchView
            .setOnQueryTextListener(this)
        setupRecyclerView(factRv)


        var funFactsViewModel: FunFactsViewModel = ViewModelProvider(
            this,
            FunFactsViewModelFactory(application, FunFactRepository(this, Dispatchers.IO))
        )[FunFactsViewModel::class.java]

        funFactsViewModel.getCurrentFact().observe(this, Observer {
            displayText.text = it.data?.text ?: "fact not found"
        })

        funFactsViewModel.getCachedFacts().observe(this, Observer {
            renderBookList(it)
        })

        downloadButton.setOnClickListener {
            funFactsViewModel.updateFact(true)
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
                factRv.visibility = View.VISIBLE
                searchView.visibility = View.VISIBLE

            } else {
                displayText.visibility = View.VISIBLE
                showAll.text = getString(R.string.show_all_facts)
                saveButton.visibility = View.VISIBLE
                downloadButton.visibility = View.VISIBLE
                factRv.visibility = View.GONE
                searchView.visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView(rv: RecyclerView) {
        rv.addItemDecoration(
            DividerItemDecoration(
                rv.context,
                DividerItemDecoration.VERTICAL
            )
        )
        rv.setHasFixedSize(true)

        rv.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        factAdapter = FunFactAdapter()
        rv.adapter = factAdapter
    }

    private fun renderBookList(bookList: List<FunFact>) {
        factAdapter.addData(bookList)
        factAdapter.notifyDataSetChanged()

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        factAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        factAdapter.filter.filter(newText)
        return false
    }

}