package com.test.funfactsapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FunFact::class], version = 1)
abstract class FunFactsDataBase : RoomDatabase() {
    abstract fun getFunFactDao(): FunFactsDao
}