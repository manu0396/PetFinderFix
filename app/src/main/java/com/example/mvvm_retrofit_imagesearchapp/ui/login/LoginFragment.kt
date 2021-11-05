package com.example.mvvm_retrofit_imagesearchapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.databinding.FragmentLoginBinding
import com.example.mvvm_retrofit_imagesearchapp.databinding.FragmentDetailBinding
import com.example.mvvm_retrofit_imagesearchapp.diologs.LoginDialog
import com.example.mvvm_retrofit_imagesearchapp.diologs.RegisterDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginFragment: Fragment() {

    private val RC_SIGN_IN = 123
    private lateinit var navController : NavController

    private lateinit var btn_google: MaterialButton
    private lateinit var btn_register: MaterialButton
    private lateinit var btn_login: MaterialButton

    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding!!

    private var diolog_register: RegisterDialog? = null
    private var diolog_login: LoginDialog? = null

    private var firebaseAuth :  FirebaseAuth? = null
    private var mGoogleSignIn: GoogleSignInClient? = null
    private var firebaseUser : FirebaseUser? = null
    override fun onStart() {
        super.onStart()
        FirebaseApp.initializeApp(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View  = binding.root

        initView(root)
        
        initListener()
        
        return root
    }

    private fun initListener() {

        val loginActivity: LoginFragment = this
        btn_google?.setOnClickListener(View.OnClickListener {
            createRequest()
            signIn()
        })
        btn_register?.setOnClickListener(View.OnClickListener {
            diolog_register = RegisterDialog(firebaseAuth, loginActivity)
            fragmentManager?.let { it1 -> diolog_register!!.show(it1, "RegisterDiolog") }
        })
        btn_login?.setOnClickListener(View.OnClickListener {
            diolog_login = LoginDialog(firebaseAuth)
            fragmentManager?.let { it1 -> diolog_login!!.show(it1, "LoginDiolog") }
        })
    }

    private fun initView(root: View) {

        btn_google = root.findViewById(R.id.btn_google)
        btn_register = root.findViewById(R.id.btn_register)
        btn_login = root.findViewById(R.id.btn_login)

        //Setup Firebase
        context?.let { FirebaseApp.initializeApp(it) }
        firebaseAuth = FirebaseAuth.getInstance()

    }
    private fun signIn() {
        val signInIntent = mGoogleSignIn!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
        showHome()
    }

    private fun showHome() {
        navController = Navigation.findNavController(this.requireView())
        navController.navigate(R.id.action_loginFragment_to_galleryFragment)
        Toast.makeText(context, "Login completado correctamente", Toast.LENGTH_LONG).show()
    }

    private fun createRequest() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("786240463795-o92gljk39cmdd0pra162arebskntkdu5.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignIn = GoogleSignIn.getClient(this.activity, gso)
    }
}