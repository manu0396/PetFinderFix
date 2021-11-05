package com.example.mvvm_retrofit_imagesearchapp.room

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.liveData
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mvvm_retrofit_imagesearchapp.data.RoomUnsplashPhoto
import javax.inject.Inject

class RoomRepository @Inject constructor (private val context: Context, private val db: PhotoDatabase){
    private lateinit var photoDao: PhotoDAO
    private lateinit var allPhotos: LiveData<List<RoomResponse>>
    //private var photoUser: LiveData<List<RoomResponse>>? = null

    init {
        val database: PhotoDatabase? = PhotoDatabase.getDatabase(context)
        photoDao = database!!.photoDao()
        allPhotos = photoDao.loadAll()
        //photoUser = photoDao.selectPhoto(user.user.toString())
    }
    //methods for database operations :-
    fun insert(photo: RoomUnsplashPhoto?):Long {
        InsertNoteAsyncTask(photoDao).execute(photo)

        val userIdAdd = db.photoDao().insertPhoto(photo!!)
        return userIdAdd
    }

    fun delete(photo: RoomUnsplashPhoto?) {
        db.photoDao().deletePhoto(photo)
    }

    fun getAllPhotos(): LiveData<List<RoomResponse>> {
        return allPhotos
    }
    fun getPhotoUser(user:String): LiveData<List<RoomResponse>>? {
        //TODO()
        var resp = db.photoDao().selectPhoto(user)
        //var data = resp.value
        return resp
     }

    private class InsertNoteAsyncTask(photoDao: PhotoDAO) :
        AsyncTask<RoomUnsplashPhoto?, Void?, Void?>() {
        //static : doesnt have reference to the
        // repo itself otherwise it could cause memory leak!
        private val photoDAO: PhotoDAO

        init {
            this.photoDAO = photoDao
        }

        override fun doInBackground(vararg photos: RoomUnsplashPhoto?): Void? {
            photoDAO.insertPhoto(photos[0]!!) //single note
            return null
        }
    }
}

