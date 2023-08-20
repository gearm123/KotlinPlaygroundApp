package com.test.funfactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.test.funfactsapp.data.FunFactRepository
import com.test.funfactsapp.db.FunFact

class FunFactsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FunFactRepository.getInstance(application)

    fun getFunFacts(): List<FunFact>? {
        return repository.getFactsFromDb()
    }

    suspend fun getFact(new: Boolean): String? {
        if (new) {
            return repository.downloadFact()
        }
        val facts: List<FunFact>? = repository.getFactsFromDb()
        if (facts?.isNotEmpty() == true) {
            return facts[0].text
        }
        return null
    }

    fun saveFact(funFact: FunFact) {
        repository.insert(funFact)
    }
}