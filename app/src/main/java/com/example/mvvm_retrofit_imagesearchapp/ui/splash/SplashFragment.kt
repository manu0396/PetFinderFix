package com.example.mvvm_retrofit_imagesearchapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.MainActivity

import android.content.Intent
import android.os.Handler
import androidx.navigation.NavController
import androidx.navigation.Navigation


class SplashFragment: Fragment(R.layout.fragment_splash) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO(Utilizar el nav-graph.xml)
        Handler().postDelayed(Runnable { //This method will be executed once the timer is over
            // Start your login
            val navController = Navigation.findNavController(view!!)
            navController.navigate(R.id.action_splashFragment_to_loginFragment)
            // close this activity
            //finish()
        }, 3000)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}