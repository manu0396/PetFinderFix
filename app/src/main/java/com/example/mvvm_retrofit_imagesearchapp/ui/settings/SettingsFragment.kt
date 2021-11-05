package com.example.mvvm_retrofit_imagesearchapp.ui.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings){

    private var mAuth : FirebaseAuth? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recatamos el usuario autenticado para tener acceso a su configuración de usuario.
        mAuth = FirebaseAuth.getInstance()

        if(mAuth != null && mAuth!!.currentUser?.email.toString() != "null"){
            tvTitleSettings.text =  getString(R.string.welcome_messages, mAuth!!.currentUser?.email.toString());
        }
        val binding = FragmentSettingsBinding.bind(view)
        binding.apply {
            btnCloseSesion.setOnClickListener(View.OnClickListener {
                setUpDiolog()

            })
            btnChangePass.setOnClickListener(View.OnClickListener {
                mAuth!!.sendPasswordResetEmail(mAuth!!.currentUser?.email.toString())
                Toast.makeText(context, "Se ha enviado un correo para restablecer su constraseña", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun setUpDiolog() {
        val builder = MaterialAlertDialogBuilder(context!!)
        // dialog title
        builder.setTitle("Confirm")
        // drawable for dialog title
        builder.setIcon(R.drawable.ic_login_logo)
        // dialog message
        builder.setMessage("¿Cerrar Sesión?")
        // dialog background color
        builder.background = ColorDrawable(Color.parseColor("#FEFEFA"))
        // icon for positive button
        builder.setPositiveButtonIcon(
            getDrawable(context!!, R.drawable.ic_check)
        )
        builder.setNeutralButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        builder.setNegativeButtonIcon(
            getDrawable(context!!, R.drawable.ic_circle_cross)
        )
        builder.setPositiveButton("Acept"){ dialog,which->
            // do something on positive button click
            Toast.makeText(context, "Cerrando sesión", Toast.LENGTH_LONG).show()
            mAuth!!.signOut()
            val navController = Navigation.findNavController(view!!)
            navController.navigate(R.id.action_settingsFragment_to_loginFragment)
        }
        // set dialog non cancelable
        builder.setCancelable(true)
        // finally, create the alert dialog and show it
        val dialog = builder.create()
        dialog.show()
    }

}