package com.example.mvvm_retrofit_imagesearchapp.api.retrofit

import com.example.mvvm_retrofit_imagesearchapp.data.UnsplashPhoto

data class UnsplashResponse(
    val results : List<UnsplashPhoto>
)