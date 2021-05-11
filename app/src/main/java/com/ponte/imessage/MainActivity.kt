package com.ponte.imessage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { //
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerButton.setOnClickListener {
           performRegistration()
        }

        alreadyHaveAccountRegister.setOnClickListener {
            Log.d("Main Activity", "Show Login Activity")
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegistration () {

        var email = emailEditTextRegister.text.toString()
        var password = passwordEditTextRegister.text.toString()

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "Please insert email and password", Toast.LENGTH_LONG).show()
            return
        }

        Log.d("Main Activity", "Email is $email")
        Log.d("Main Activity", "Password is $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password) //aggiungiamo dei listener per il successo (ovvero richiesta mandata con successo)
            .addOnCompleteListener {
                if (!it.isSuccessful) {  //da vedere se ha davverro avuto successo
                    return@addOnCompleteListener
                }
                //se va a buon fine
                Log.d("Main Activity", "Fatto")
            }
            //richiesta non mandata per un errore
            .addOnFailureListener {
                Toast.makeText(this, "Failed ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("Main Activity", "failed ${it.message}")
            }
            }
    }
