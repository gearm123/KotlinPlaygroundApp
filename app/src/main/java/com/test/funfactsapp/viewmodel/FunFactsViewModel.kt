package com.test.funfactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.test.funfactsapp.data.FunFactRepository
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.network.FunFactApiState
import kotlinx.coroutines.launch

class FunFactsViewModel(
    application: Application,
    private val repository: FunFactRepository,
) : AndroidViewModel(application) {

    private val cachedFactsList: MutableLiveData<List<FunFact>> = MutableLiveData()
    private val currentFact: MutableLiveData<FunFactApiState<FunFact>> = MutableLiveData()

    init {
        loadCachedFacts()
        updateFact(false)
    }

    fun updateFact(
        new: Boolean
    ) {
        viewModelScope.launch() {
            if (new) {
                currentFact.postValue(repository.downloadFact())
                // If Api call is succeeded, set the State to Success
                // and set the response data to data received from api
            } else {
                currentFact.postValue(repository.getFactFromDb(0))
            }
        }
    }

    private fun loadCachedFacts() {
        viewModelScope.launch() {
            cachedFactsList.postValue(repository.getFactsFromDb())
        }
    }

    fun saveFact(funFact: FunFact) {
        viewModelScope.launch() {
            repository.insert(funFact)
        }
        loadCachedFacts()
    }

    fun getCurrentFact(): LiveData<FunFactApiState<FunFact>> {
        return currentFact
    }

    fun getCachedFacts(): LiveData<List<FunFact>> {
        return cachedFactsList
    }

}