package com.test.funfactsapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.db.FunFactsDao
import com.test.funfactsapp.db.FunFactsDataBase
import com.test.funfactsapp.network.ApiAdapter
import com.test.funfactsapp.network.FunFactApiState
import com.test.funfactsapp.network.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Response


class FunFactRepository(
    context: Context,
    private val myDispatcher: CoroutineDispatcher
) {
    private var funFactDao: FunFactsDao

    init {
        val db = Room.databaseBuilder(
            context,
            FunFactsDataBase::class.java, "database-name"
        ).build()
        funFactDao = db.getFunFactDao()
    }


    suspend fun insert(funFact: FunFact) {
        withContext(myDispatcher) {
            funFactDao.insert(funFact)
        }
    }

    suspend fun getFactFromDb(id: Int): FunFactApiState<FunFact> {
        var apiState: FunFactApiState<FunFact> =
            FunFactApiState(Status.ERROR, null, "")
        val allFacts: List<FunFact>
        withContext(myDispatcher) {
            allFacts = funFactDao.getAllEvents()
            apiState = FunFactApiState(Status.SUCCESS, allFacts[0], "")
        }
        return apiState
    }

    suspend fun getFactsFromDb(): List<FunFact> {
        var funFactList: List<FunFact> = listOf()
        withContext(myDispatcher) {
            funFactList = funFactDao.getAllEvents()
        }
        return funFactList
    }

    suspend fun downloadFact(): FunFactApiState<FunFact> {
        var apiState: FunFactApiState<FunFact> =
            FunFactApiState(Status.ERROR, null, "")
        withContext(myDispatcher) {
            var response: FunFact? = null
            try {
              response  = ApiAdapter.apiClient.getRandomFact()?.body()
            }catch (e : java.lang.Exception){

            }
            response?.also {
                apiState = FunFactApiState(
                    Status.SUCCESS,
                    response,
                    ""
                )
            }
        }
        return apiState
    }
}