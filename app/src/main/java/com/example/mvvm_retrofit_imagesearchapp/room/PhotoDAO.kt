package com.example.mvvm_retrofit_imagesearchapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvm_retrofit_imagesearchapp.data.RoomUnsplashPhoto
import com.example.mvvm_retrofit_imagesearchapp.utils.Global
import androidx.room.Delete

@Dao
interface PhotoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhoto(photo: RoomUnsplashPhoto): Long
    @Query("SELECT * FROM ${Global.databaseName}")
    fun loadAll():LiveData<List<RoomResponse>>

    @Delete
    fun deletePhoto(photo: RoomUnsplashPhoto?)
    @Query("SELECT * FROM ${Global.databaseName} WHERE email=:email")
    fun selectPhoto(email: String): LiveData<List<RoomResponse>>
}