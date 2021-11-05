package com.example.mvvm_retrofit_imagesearchapp.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.adapter.gallery.UnsplashPhotoAdapter
import com.example.mvvm_retrofit_imagesearchapp.adapter.gallery.UnsplashPhotoLoadStateAdapter
import com.example.mvvm_retrofit_imagesearchapp.api.retrofit.UnsplashApi
import com.example.mvvm_retrofit_imagesearchapp.data.UnsplashPhoto
import com.example.mvvm_retrofit_imagesearchapp.data.UnsplashRepository
import com.example.mvvm_retrofit_imagesearchapp.databinding.FragmentGalleryBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


//NO instancio el GalleryViewModel a traves del ViewModelFactory, porque no se instanciar UnsplashRepository

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.OnItemClickLisener {
    private val viewModel by viewModels<GalleryViewModel>()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance()?.currentUser
        if(user != null){
            Toast.makeText(context, "Already signIn", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context, "No se ha detectado ningún usuario", Toast.LENGTH_LONG).show()
        }
    }
    override fun onItemClick(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailFragment(photo)
        findNavController().navigate(action)
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_gallery, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        val menu_diary = menu.findItem(R.id.menu_diary)
        menu_diary.setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener {
            val navController = Navigation.findNavController(view!!)
            navController?.navigate(R.id.action_galleryFragment_to_diaryFragment)
            return@OnMenuItemClickListener false
        })

        val menu_settings = menu.findItem(R.id.menu_settings)
        menu_settings.setOnMenuItemClickListener ( MenuItem.OnMenuItemClickListener {
            val navController =Navigation.findNavController(view!!)
            navController?.navigate(R.id.action_galleryFragment_to_settingsFragment)
            return@OnMenuItemClickListener false
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null && query.length >= 3){
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                } else
                    Toast.makeText(activity,"Type at least 3 characters to search",Toast.LENGTH_SHORT).show()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)
        val adapter = UnsplashPhotoAdapter(this)
        //SetUp the view initially
        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
            )
            buttonRetry.setOnClickListener {
                adapter.retry()
            }
        }

        //Set MVVM
        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
        adapter.addLoadStateListener { loadState ->
            //Update the UI after loading
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error
                //Empty View
                if(loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && adapter.itemCount <= 0){
                    recyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else textViewEmpty.isVisible = false
            }
        }
        setHasOptionsMenu(true)
    }
}