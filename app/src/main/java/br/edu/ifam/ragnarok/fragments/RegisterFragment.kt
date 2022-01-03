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
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var fAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_register, container, false)

        username = view.findViewById(R.id.et_username)
        password = view.findViewById(R.id.et_password)
        confirmPassword = view.findViewById(R.id.et_confirm_password)
        fAuth = Firebase.auth

        view.findViewById<Button>(R.id.btn_login_reg).setOnClickListener {
            var navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(LoginFragment(), false)
        }

        view.findViewById<Button>(R.id.btn_register_reg).setOnClickListener {
            validateEmptyForm()
        }
        return view
    }

    private fun firebaseSignUp() {
        btn_register_reg.isEnabled = false
        btn_register_reg.alpha = 0.5f
        fAuth.createUserWithEmailAndPassword(username.text.toString(),
            password.text.toString()).addOnCompleteListener {
            task ->
            if (task.isSuccessful) {
                var navHome = activity as FragmentNavigation
                navHome.navigateFrag(HomeFragment(), true)
            } else {
                btn_register_reg.isEnabled = true
                btn_register_reg.alpha = 1.0f
                Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateEmptyForm() {
        val icon = AppCompatResources.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_warning_24
        )

        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when {
            TextUtils.isEmpty(username.text.toString().trim()) -> {
                username.setError("Por favor digite o endereço de e-mail", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim()) -> {
                password.setError("Por favor digite a senha", icon)
            }
            TextUtils.isEmpty(confirmPassword.text.toString().trim()) -> {
                confirmPassword.setError("Por favor digite a senha novamente", icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.text.toString().isNotEmpty() &&
                    confirmPassword.text.toString().isNotEmpty() -> {
                if (username.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))) {
                    if (password.text.toString().length >= 5) {
                        if (password.text.toString() == confirmPassword.text.toString()) {
                            firebaseSignUp()
                        } else {
                            confirmPassword.setError(
                                "A senha que você digitou não é idêntica",
                                icon
                            )
                        }
                    } else {
                        password.setError("A senha deve ter no mínimo 5 caracteres", icon)
                    }
                } else {
                    username.setError("Por favor digite um endereço de e-mail válido", icon)
                }
            }
        }
    }
}