package com.example.mvvm_retrofit_imagesearchapp.ui

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_retrofit_imagesearchapp.data.UnsplashRepository
import com.example.mvvm_retrofit_imagesearchapp.room.PhotoDAO
import com.example.mvvm_retrofit_imagesearchapp.room.RoomRepository
import com.example.mvvm_retrofit_imagesearchapp.ui.detail.DetailViewModel
import com.example.mvvm_retrofit_imagesearchapp.ui.diary.DiaryViewModel
import com.example.mvvm_retrofit_imagesearchapp.ui.gallery.GalleryViewModel

class ViewModelFactory
    (
    private val room_repository: RoomRepository?,
    private val api_repository: UnsplashRepository?
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(room_repository!!) as T
            }
            modelClass.isAssignableFrom(DiaryViewModel::class.java) -> {
                DiaryViewModel(room_repository!!) as T
            }
            modelClass.isAssignableFrom(GalleryViewModel::class.java) -> {
                GalleryViewModel(api_repository!!, SavedStateHandle()) as T
            }
                else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}