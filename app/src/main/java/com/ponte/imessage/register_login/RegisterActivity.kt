package com.ponte.imessage.register_login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.FirebaseStorage
import com.ponte.imessage.R
import com.ponte.imessage.messages.LatestMessageActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class RegisterActivity : AppCompatActivity() {

    var selectedPhotoUri: Uri? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) { //
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener {
           performRegistration()
        }

        alreadyHaveAccountRegister.setOnClickListener {
            Log.d("Main Activity", "Show Login Activity")
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/"
            startActivityForResult(intent, 0)
        }

       /* val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_STANDARD)

        signInButton.setOnClickListener{

            // Configure Google Sign In
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

            auth = Firebase.initialize()

        }*/
    }

    /*private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        val RC_SIGN_IN
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "Foto selezionata")
            selectedPhotoUri = data.data //location di dove l'immagine e' stata memorizzata
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            /*val bitmapDrawable = BitmapDrawable(bitmap)
            buttonSelectPhoto.setBackgroundDrawable(bitmapDrawable) //guaradare come ha messo l'immagine giusta
             */
            imageViewRegister.setImageBitmap(bitmap)  //carichiamo il bitmap sopra l'imageView inserita con la lireria esterna
            buttonSelectPhoto.alpha = 0f  //bottone trasparente cosi' da far vedere solo l'immagine
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
                uploadImageToFirebase()
            }
            //richiesta non mandata per un errore
            .addOnFailureListener {
                Toast.makeText(this, "Failed ${it.message}", Toast.LENGTH_LONG).show()
                Log.d("Main Activity", "failed ${it.message}")
            }
    }

    private fun uploadImageToFirebase() {
        if(selectedPhotoUri == null)
            return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$filename")
        ref.putFile(selectedPhotoUri!!)   //con i due punti esclamativi forziamo il fatto che l'oggetto non e' nullo
            .addOnSuccessListener {
                Log.d("Register Activity", "immagine caricata")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Register Activity", "File location $it")
                    //todo salvare l'utente nel db
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener{
                Toast.makeText(this, "Qualcosa e' andato storto", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveUserToFirebaseDatabase (profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid") //cosi' creo la cartella dell'utente, perche' firebase non e' relazionale
        val user = User(uid, usernameEditTextRegister.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register Activity", "Utente salvato")

                //dopo la creazione dell'utente
                val intent = Intent(this, LatestMessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK) //serve per non farci andare nella schermata di login/registrazione da loggati
                startActivity(intent)
            }
            .addOnFailureListener{
                Toast.makeText(this, "Impossibile savare l'utente ${it.message}", Toast.LENGTH_LONG).show()
            }
    }

}

class User( val uid:String, val username:String, val profileImageUrl:String) {
    constructor() : this ("", "", "")  //necessario per non far dare errore a firebase quando scarichiamo utenti
}