package com.test.funfactsapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fun_facts_table")
data class FunFact(
    @PrimaryKey val factId: Int,
    val text: String
) {
}