package com.test.funfactsapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.funfactsapp.data.FunFactRepository
import kotlinx.coroutines.CoroutineDispatcher

class FunFactsViewModelFactory constructor(
    private val application: Application,
    private val repository: FunFactRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FunFactsViewModel::class.java!!)) {
            FunFactsViewModel(application, repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}