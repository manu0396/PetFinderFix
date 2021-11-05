package com.example.mvvm_retrofit_imagesearchapp.diologs


import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.mvvm_retrofit_imagesearchapp.R
import com.example.mvvm_retrofit_imagesearchapp.ui.login.LoginFragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RegisterDialog(private val firebaseAuth: FirebaseAuth?, loginActivity: LoginFragment?) :
    DialogFragment() {
    private var register_cancel: ImageView? = null
    private var register_mail_input_layout: TextInputLayout? = null
    private var register_pass_input_layout: TextInputLayout? = null
    private var register_new_pass_input_layout: TextInputLayout? = null
    private var register_mail_input_edit: TextInputEditText? = null
    private var register_pass_input_edit: TextInputEditText? = null
    private var register_new_pass_input_edit: TextInputEditText? = null
    private var btn_register: MaterialButton? = null
    private var loginActivity: LoginFragment? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(activity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.dialog_register)
        }
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_register, null)
        builder.setView(view)
            .setCancelable(true)
        val dialog = builder.create()
        dialog.show()
        initView(view)
        initListeners()
        return dialog
    }

    private fun initView(view: View) {
        register_cancel = view.findViewById<View>(R.id.register_cancel) as ImageView
        register_mail_input_layout =
            view.findViewById<View>(R.id.register_mail_input_layout) as TextInputLayout
        register_mail_input_edit =
            view.findViewById<View>(R.id.register_mail_input_edit) as TextInputEditText
        register_pass_input_layout =
            view.findViewById<View>(R.id.register_pass_input_layout) as TextInputLayout
        register_pass_input_edit =
            view.findViewById<View>(R.id.register_pass_input_edit) as TextInputEditText
        register_new_pass_input_layout =
            view.findViewById<View>(R.id.register_new_pass_input_layout) as TextInputLayout
        register_new_pass_input_edit =
            view.findViewById<View>(R.id.register_new_pass_input_edit) as TextInputEditText
        btn_register = view.findViewById<View>(R.id.btn_register) as MaterialButton
        btn_register!!.visibility = View.INVISIBLE
    }

    private fun initListeners() {
        register_cancel!!.setOnClickListener { dismiss() }
        register_mail_input_layout!!.setEndIconOnClickListener { register_mail_input_edit!!.text!!.clear() }
        register_pass_input_layout!!.setEndIconOnClickListener { register_pass_input_edit!!.text!!.clear() }
        register_new_pass_input_layout!!.setEndIconOnClickListener { register_new_pass_input_edit!!.text!!.clear() }
        register_pass_input_edit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                validatePassword()
            }
        })
        register_new_pass_input_edit!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                validatePassword()
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                validatePassword()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        btn_register!!.setOnClickListener { registerUser() }
    }

    private fun registerUser() {
        firebaseAuth?.createUserWithEmailAndPassword(register_mail_input_edit!!.text.toString(),
            register_pass_input_edit!!.text.toString())
            ?.addOnFailureListener { e ->
                register_mail_input_layout!!.error = e.message
                e.printStackTrace()
            }
            ?.addOnSuccessListener {
                firebaseAuth.signInWithEmailAndPassword(register_mail_input_edit!!.text.toString(),
                    register_pass_input_edit!!.text.toString())
                    .addOnSuccessListener {
                        //Navigate to LoginDiolog.
                        Toast.makeText(context, "Registro realizado correctamente", Toast.LENGTH_LONG).show()

                        dismiss() }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                    }
            }
    }

    private fun validatePassword() {
        if (register_pass_input_edit!!.text.toString() == register_new_pass_input_edit!!.text.toString()) register_new_pass_input_layout!!.isErrorEnabled =
            false else {
            register_new_pass_input_layout!!.isErrorEnabled = true
            register_new_pass_input_layout!!.error = getString(R.string.no_matching_pwds)
            btn_register!!.visibility = View.INVISIBLE
        }
        if (register_pass_input_edit!!.text.toString()
                .isEmpty() || register_new_pass_input_edit!!.text.toString().isEmpty()
        ) btn_register!!.visibility =
            View.INVISIBLE else btn_register!!.visibility = View.VISIBLE
    }

    init {
        if (loginActivity != null) {
            this.loginActivity = loginActivity
        }
    }
}