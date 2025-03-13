package com.example.login

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity_Login::class], version = 1)
abstract class RoomDB_Login: RoomDatabase() {

    abstract fun loginDao(): RoomDao_Login
}