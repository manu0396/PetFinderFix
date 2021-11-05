package com.example.mvvm_retrofit_imagesearchapp.ui.diary

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.adapter.diary.AdapterDiary
import com.example.mvvm_retrofit_imagesearchapp.data.UnsplashPhoto
import com.example.mvvm_retrofit_imagesearchapp.databinding.FragmentDiaryBinding
import com.example.mvvm_retrofit_imagesearchapp.room.PhotoDatabase
import com.example.mvvm_retrofit_imagesearchapp.room.RoomRepository
import com.example.mvvm_retrofit_imagesearchapp.room.RoomResponse
import com.example.mvvm_retrofit_imagesearchapp.ui.ViewModelFactory
import com.example.mvvm_retrofit_imagesearchapp.ui.detail.DetailViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_diary.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryFragment : Fragment(R.layout.fragment_diary), AdapterDiary.OnItemClickListenerDiary{
    private lateinit var viewModel: DiaryViewModel
    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var user: String
    private lateinit var mAuth : FirebaseAuth
    private var adapter: AdapterDiary? = null
    private lateinit var repository: RoomRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDiaryBinding.bind(view)
        initViewModel()
        //Al  Listener le paso una instancia a partir del observer.
        //mientras que al listener del BtnRetry, le paso el atributo de clase.
        initListener()
    }

    private fun initListener() {
        mAuth.addAuthStateListener {
            //TODO() Debug to see if the object is null
            if (null != it.currentUser) {
                user = it.currentUser!!.email.toString()
                checkUser(user)
            } else {
                Toast.makeText(context, "User still be null", Toast.LENGTH_LONG).show()
            }
        }
        val btnRetryDiary = binding.btnRetryDiary
        btnRetryDiary.setOnClickListener(View.OnClickListener {
            user = mAuth.currentUser?.email.toString()
            checkUser(user)
        })
    }
    private fun initViewModel() {
        //Para Instanciar el ViewModel, utilizamos el ViewModelFactory
        repository = RoomRepository(context!!, PhotoDatabase.getDatabase(context!!)!!)
        val viewModelFactory = repository?.let { ViewModelFactory(it, null) }
        viewModel = ViewModelProviders.of(this, viewModelFactory)[DiaryViewModel::class.java]
    }
    fun checkUser(user:String){
        if(user!="null"){
            btnRetryDiary.visibility=View.GONE
            Toast.makeText(context, "Already Sign in !", Toast.LENGTH_LONG).show()
            val db = PhotoDatabase.getDatabase(context!!)
            viewModel.getPhotoUser(user)!!.observe(viewLifecycleOwner){
                       // searchPhoto(db!!, user)
                adapter = AdapterDiary(context!!.applicationContext, it, this)
                binding.apply {
                    recycleViewDiary.setHasFixedSize(true)
                    recycleViewDiary.itemAnimator = null
                    recycleViewDiary.adapter = adapter
                }
            }
            setHasOptionsMenu(true)
        }else{
            Toast.makeText(context, "¡¡ User esta a null !!", Toast.LENGTH_LONG).show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(photo: RoomResponse) {

        //TODO("Crear un objeto UnsplashPhoto a partir del RoomResponse")
        //TODO("Aleternativa: Crear un screen personalizado donde el argument sea de tipo RoomResponse")
        val action = DiaryFragmentDirections.actionDiaryFragmentToDetailFragmentAdd(photo)
        findNavController().navigate(action)
    }
}
