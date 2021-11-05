package com.example.mvvm_retrofit_imagesearchapp.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.data.RoomUnsplashPhoto
import com.example.mvvm_retrofit_imagesearchapp.databinding.FragmentDetailBinding
import com.example.mvvm_retrofit_imagesearchapp.room.PhotoDatabase
import com.example.mvvm_retrofit_imagesearchapp.utils.Global
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.NumberFormatException
import kotlin.math.absoluteValue

import androidx.appcompat.content.res.AppCompatResources
import android.content.DialogInterface
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm_retrofit_imagesearchapp.room.RoomRepository
import com.example.mvvm_retrofit_imagesearchapp.ui.ViewModelFactory

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail){

    private val args by navArgs<DetailFragmentArgs>()
    private var email: String? = null
    private lateinit var mAuh: FirebaseAuth
    private lateinit var viewModel : DetailViewModel
    private lateinit var repository: RoomRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: (AndroidInject.inject(this)) puede que solucione el problema de instanciar el ViewModel.
        try {
            mAuh = Firebase.auth
            if(mAuh!= null){
                email = mAuh.currentUser!!.email.toString()
            }else{
                Toast.makeText(context, "No se ha detectado ningun usuario", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        setUpViewModel()
    }

    private fun initView(view: View) {
        val binding = FragmentDetailBinding.bind(view)
        binding.apply {
            val photo = args.photo
            var room_photo: RoomUnsplashPhoto? = null
            try {
                room_photo = RoomUnsplashPhoto(
                    0,
                    photo.id!!,
                    photo.description,
                    photo.urls.full,
                    photo.user.name,
                    email
                )
            } catch (e: NumberFormatException) {
                Toast.makeText(context, "¡ Falla la conversion !", Toast.LENGTH_LONG).show()
                Log.d("NumberFormat", e.toString())
            }
            Glide.with(this@DetailFragment)
                .load(photo.urls.regular)
                .error(R.drawable.ic_error)
                .override((Target.SIZE_ORIGINAL) / 4)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textviewDesc.isVisible = photo.description != null
                        textviewCreator.isVisible = true
                        return false
                    }
                })
                .into(imageView)
            textviewDesc.text = photo.description
            val uri = Uri.parse(photo.user.attributionUrl)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            //set the listener to Room

            btnAdd.setOnClickListener(View.OnClickListener {
                //Invocamos dentro de una corrutina el método que llamará al viewModel
                GlobalScope.launch {
                    insertPhotoWithConfirm(room_photo!!)
                }
            })
            textviewCreator.apply {
                text = "Photo by ${photo.user.name} on Unsplash"
                setOnClickListener {
                    context.startActivity(intent)
                }
                paint.isUnderlineText = true
            }
        }
    }

    private fun setUpViewModel() {

        repository = RoomRepository(context!!, PhotoDatabase.getDatabase(context!!)!!)
        val viewModelFactory = repository?.let { ViewModelFactory(it, null) }
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[DetailViewModel::class.java]
    }

    //TODO("Explota en un 2º insert al invocar el Looper.prepare()")
    private suspend fun insertPhotoWithConfirm(room_photo: RoomUnsplashPhoto ) {
        withContext(Dispatchers.IO) {
            Log.d("btnAdd", "Entra en el hilo Antes del Looper")
            Looper.prepare()
            //Si existe el objeto a añadir.
            Log.d("btnAdd", "Entra en el hilo")
            if (room_photo != null) {
                Log.d("btnAdd", "Entra en el if")
                //**Observamos la inserciones que realize el usuario **
                var addedId = viewModel.insert(room_photo)
                Log.d("btnAdd", "Añade la foto")
                //Si devuelve algún parámetro diferente a -1, la inserción se habra producido correctamente
                //Confirmamos la operración.
                if(addedId.toInt() !== -1){
                    Log.d("btnAdd", "Entra en el if addedId")
                    Toast.makeText(context, "¡ Nueva mascota añadida !", Toast.LENGTH_LONG).show()
                    Looper.loop()
                    Log.d("btnAdd", "Ejecuta el Toast")
                    //Si no pertenece al usuario
                }else{
                    Toast.makeText(context, "Item y usuario no coincidentes", Toast.LENGTH_LONG).show()
                    Looper.loop()
                }
                //Si no existe la foto
            }else {
              Toast.makeText(context, "No se ha podido localizar la foto a añadir", Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        }
    }
}