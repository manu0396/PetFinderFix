package com.example.mvvm_retrofit_imagesearchapp.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mvvm_retrofit_imagesearchapp.data.RoomUnsplashPhoto
import com.example.mvvm_retrofit_imagesearchapp.room.PhotoDatabase
import com.example.mvvm_retrofit_imagesearchapp.room.RoomRepository
import com.example.mvvm_retrofit_imagesearchapp.room.RoomResponse

class DetailViewModel (private var repository: RoomRepository) : AndroidViewModel(Application()) {

    private var allPhotos : LiveData<List<RoomResponse>>? = null
    private var photoUser : LiveData<List<RoomResponse>>? = null
    private var db : PhotoDatabase = PhotoDatabase.getDatabase(getApplication())!!

    init {
        repository = RoomRepository(getApplication(), db)
        allPhotos = repository!!.getAllPhotos()
        //photoUser = repository!!.getPhotoUser(user.user.toString())
    }
    fun insert(photo: RoomUnsplashPhoto?):Long {
        return repository!!.insert(photo)
    }
    fun getAllPhotos(): LiveData<List<RoomResponse>>? {
        return allPhotos
    }
    fun delete(photo: RoomUnsplashPhoto?) {
        repository!!.delete(photo)
    }
    fun getPhotoUser(user: String): LiveData<List<RoomResponse>>?{
        photoUser = repository?.getPhotoUser(user)
        return photoUser
    }
}