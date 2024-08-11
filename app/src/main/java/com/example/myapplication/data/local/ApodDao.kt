package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ApodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apod: APODEntity)

    @Query("SELECT * FROM apod_table WHERE date = :date LIMIT 1")
    suspend fun getApodByDate(date: String): APODEntity?
}
