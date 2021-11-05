package com.example.mvvm_retrofit_imagesearchapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.mvvm_retrofit_imagesearchapp.data.RoomUnsplashPhoto

import com.example.mvvm_retrofit_imagesearchapp.utils.Global

@Database(entities = [RoomUnsplashPhoto::class], version=1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {
    abstract fun photoDao():PhotoDAO

    companion object {
        private var INSTANCE: PhotoDatabase? = null
        open fun getDatabase(context: Context): PhotoDatabase? {
            if (null == INSTANCE) {
                synchronized(PhotoDatabase::class.java) {
                    if (null == INSTANCE) {
                        INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            PhotoDatabase::class.java,
                            Global.databaseName
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }
}