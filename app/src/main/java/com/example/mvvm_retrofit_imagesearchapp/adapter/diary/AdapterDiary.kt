package com.example.mvvm_retrofit_imagesearchapp.adapter.diary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.databinding.ItemUnsplashPhotoBinding
import com.example.mvvm_retrofit_imagesearchapp.room.RoomResponse

class AdapterDiary (var context : Context, var list : List<RoomResponse>,private val listener: OnItemClickListenerDiary):
    RecyclerView.Adapter<AdapterDiary.PhotoDiaryViewHolder>() {

        inner class PhotoDiaryViewHolder(private val binding: ItemUnsplashPhotoBinding):
            RecyclerView.ViewHolder(binding.root){

            init {
                binding.root.setOnClickListener{
                    val position = bindingAdapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val item = list.get(position)
                        if(item != null)
                            listener.onItemClick(item)
                    }
                }
            }

            fun bind (photo: RoomResponse){
                binding.apply {
                    textViewUserName.text = photo.user.toString()
                    Glide.with(itemView)
                        .load(photo.url.toString())
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imageView)
                }
            }
        }

        override fun onBindViewHolder(holder: PhotoDiaryViewHolder, position: Int) {
            val currentItem = list.get(position)
            if(currentItem != null){
                holder.bind(currentItem)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoDiaryViewHolder {
            val binding = ItemUnsplashPhotoBinding.
            inflate(LayoutInflater.from(parent.context), parent, false)
            return PhotoDiaryViewHolder(binding)
        }

        interface OnItemClickListenerDiary{
            fun onItemClick(photo: RoomResponse)
        }

    override fun getItemCount(): Int {
        return list.size
    }
}




