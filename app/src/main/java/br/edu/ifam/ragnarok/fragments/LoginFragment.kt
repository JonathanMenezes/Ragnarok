package br.edu.ifam.ragnarok.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import br.edu.ifam.ragnarok.R
import br.edu.ifam.ragnarok.interfaces.FragmentNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var fAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_login, container, false)

        username = view.findViewById(R.id.et_username)
        password = view.findViewById(R.id.et_password)
        fAuth = Firebase.auth

        view.findViewById<Button>(R.id.btn_register).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(RegisterFragment(), false)
        }

        view.findViewById<Button>(R.id.btn_login).setOnClickListener {
            validateForm()
        }

        return view
    }

    private fun firebaseSignIn() {
        btn_login.isEnabled = false
        btn_login.alpha = 0.5f
        fAuth.signInWithEmailAndPassword(username.text.toString(),
            password.text.toString()).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    var navHome = activity as FragmentNavigation
                    navHome.navigateFrag(HomeFragment(), true)
                }else{
                    btn_login.isEnabled = true
                    btn_login.alpha = 0.5f
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm() {
        val icon = AppCompatResources.getDrawable(requireContext(),
            R.drawable.ic_baseline_warning_24)

        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(username.text.toString().trim()) -> {
                username.setError("Por favor digite o endereço de e-mail", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim()) -> {
                password.setError("Por favor digite a senha", icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() -> {


                if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    firebaseSignIn()
                } else {
                    username.setError("Por favor digite um endereço de e-mail válido", icon)
                }
            }
        }
    }
}