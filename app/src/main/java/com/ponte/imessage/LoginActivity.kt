package com.ponte.imessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            performLogin()
        }

            backToRegistration.setOnClickListener {
                finish()      //meglio cosi' che mettere un altro intent, perche' si riempirebbe troppo lo stack delle activity
            }
        }

    private fun performLogin(){
        var email = emailEditTextLogin.text.toString()
        var password = passwordEditTextLogin.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please insert email and password", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("Login Activity", "Login email is $email")
        Log.d("Login Activity", "Login pass is $password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener addOnCompletedListener@{
                if (!it.isSuccessful) return@addOnCompletedListener

                Log.d("Login Activity", "Login is succesfull")
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failure ", Toast.LENGTH_LONG).show()
            }
    }
}