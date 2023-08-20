package com.test.funfactsapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FunFactsDao {
    @Query("SELECT * from fun_facts_table ")
    fun getAllEvents(): List<FunFact>

    @Insert
    fun insert(event: FunFact)

}