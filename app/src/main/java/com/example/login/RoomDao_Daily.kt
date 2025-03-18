package com.example.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDao_Daily {

    @Insert
    suspend fun insert(daily: RoomEntity_Daily)

    @Query("SELECT * FROM daily")
    suspend fun getAll(): List<RoomEntity_Daily>

    @Query("DELETE FROM daily WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    suspend fun update(daily: RoomEntity_Daily)
}