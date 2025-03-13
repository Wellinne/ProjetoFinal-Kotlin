package com.example.login

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RoomDao_Login {

    @Query("SELECT * FROM login")
    fun getAll(): List<RoomEntity_Login>

    @Query("SELECT * FROM login WHERE username LIKE :username")
    fun getByUsername(username: String): RoomEntity_Login

    @Query("SELECT * FROM login WHERE email LIKE :email")
    fun getByEmail(email: String): RoomEntity_Login

    @Insert
    fun insert(login: RoomEntity_Login)

    @Delete
    fun delete(login: RoomEntity_Login)

    @Query("UPDATE login SET pwd =:password WHERE username LIKE :username")
    fun update(username: String, password: String)
}