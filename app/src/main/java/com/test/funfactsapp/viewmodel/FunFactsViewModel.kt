package com.test.funfactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.test.funfactsapp.data.FunFactRepository
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.db.FunFactsDataBase
import com.test.funfactsapp.network.FunFactApiState
import com.test.funfactsapp.network.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FunFactsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FunFactRepository.getInstance(application)

    val factsState = MutableStateFlow(
        FunFactApiState(
            Status.LOADING,
            listOf<FunFact>(), ""
        )
    )

    init {
        viewModelScope.launch {
            getCachedFacts()
        }
    }

    suspend fun getFact(new: Boolean): MutableStateFlow<FunFactApiState<FunFact>> {
        val factState = MutableStateFlow(
            FunFactApiState(
                Status.LOADING,
                FunFact(1, ""), ""
            )
        )
        if (new) {
            repository.downloadFact()
                // If any errors occurs like 404 not found
                // or invalid query, set the state to error
                // State to show some info
                // on screen
                .catch {
                    factState.value =
                        FunFactApiState.error(it.message.toString())
                }
                // If Api call is succeeded, set the State to Success
                // and set the response data to data received from api
                .collect {
                    factState.value = FunFactApiState.success(it.data?.body())
                }
        } else {
            repository.getFactFromDb(0)
                .catch {
                    factState.value =
                        FunFactApiState.error(it.message.toString())
                }
                .collect {
                    factState.value = FunFactApiState.success(it)
                }
        }
        return factState

    }

    private suspend fun getCachedFacts() {
        repository.getFactsFromDb()
            .catch {
                factsState.value =
                    FunFactApiState.error(it.message.toString())
            }
            .collect {
                factsState.value = FunFactApiState.success(it)
            }
    }

    fun saveFact(funFact: FunFact) {
        viewModelScope.launch (Dispatchers.IO){
            repository.insert(funFact)
        }
    }
}