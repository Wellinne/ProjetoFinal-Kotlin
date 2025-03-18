package com.example.login

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity_Daily::class], version = 1)
abstract class RoomDB_Daily : RoomDatabase() {
    abstract fun dailyDao(): RoomDao_Daily
}