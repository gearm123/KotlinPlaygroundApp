package com.test.funfactsapp.data

import android.content.Context
import androidx.room.Room
import com.test.funfactsapp.db.FunFact
import com.test.funfactsapp.db.FunFactsDao
import com.test.funfactsapp.db.FunFactsDataBase
import com.test.funfactsapp.network.ApiAdapter
import com.test.funfactsapp.network.FunFactApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


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

    suspend fun getFactFromDb(id: Int): Flow<FunFact> {
        return flow {
            val funFactList: List<FunFact> = funFactDao.getAllEvents()
            emit(funFactList[0])
        }
    }

    suspend fun getFactsFromDb(): Flow<List<FunFact>> {
        return flow {
            val funFactList: List<FunFact> = funFactDao.getAllEvents()
            emit(funFactList)
        }
    }

    suspend fun downloadFact(): Flow<FunFactApiState<Response<FunFact>>> {
        return flow {

            // get the comment Data from the api
            val factResponse = ApiAdapter.apiClient.getRandomFact()
            emit(FunFactApiState.success(factResponse))
        }
    }


}