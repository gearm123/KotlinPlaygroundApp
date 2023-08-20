package com.test.funfactsapp.data

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.db.FunFactsDao
import com.test.funfactsapp.db.FunFactsDataBase
import com.test.funfactsapp.network.ApiAdapter


class FunFactRepository(context: Context) {
    private var funFactDao: FunFactsDao

    //get from shared preferences all the events and the event count
    init {
        val db = Room.databaseBuilder(
            context,
            FunFactsDataBase::class.java, "database-name"
        ).build()
        funFactDao = db.getFunFactDao()
    }

    companion object {

        private var instance: FunFactRepository? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: FunFactRepository(context).also { instance = it }
            }
    }


    fun insert(funFact: FunFact) {
        funFactDao.insert(funFact)
    }

    fun getFactsFromDb(): List<FunFact>? {
        return  funFactDao.getAllEvents()
    }

    suspend fun downloadFact(): String? {
        return try {
            val response = ApiAdapter.apiClient.getRandomFact()
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                data.text
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


}