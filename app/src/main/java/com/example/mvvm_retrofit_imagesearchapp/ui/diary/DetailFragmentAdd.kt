package com.example.mvvm_retrofit_imagesearchapp.ui.diary

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.databinding.FragmentDetailAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragmentAdd: Fragment(R.layout.fragment_detail_add) {
    private val args by navArgs<DetailFragmentAddArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailAddBinding.bind(view)
        binding.apply {
            val photo = args.roomPhoto
            Glide.with(this@DetailFragmentAdd)
                .load(photo.url)
                .error(com.example.mvvm_retrofit_imagesearchapp.R.drawable.ic_error)
                .override((Target.SIZE_ORIGINAL)/4)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressBar.isVisible = false
                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar.isVisible = false
                        textviewDesc.isVisible = photo.user != null
                        textviewCreator.isVisible = true
                        return false
                    }
                })
                .into(imageView)
            textviewCreator.text = photo.user.toString()
            if(photo.description.toString() != "null"){
                textviewDesc.text = photo.description.toString()
            }else{
                textviewDesc.text = "No se ha encontrado la descripción"
            }

        }
    }
}