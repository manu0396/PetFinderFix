package com.example.mvvm_retrofit_imagesearchapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.mvvm_retrofit_imagesearchapp.utils.Global

@Entity(tableName= Global.databaseName, indices = [Index(value = ["uid"], unique = true)])
data class RoomUnsplashPhoto(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var uid: String = id.toString(),
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "url") var url: String?,
    @ColumnInfo(name="user") var user:String?,
    @ColumnInfo(name="email")var email:String?,
)