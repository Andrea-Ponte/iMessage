package com.ponte.imessage.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ponte.imessage.R
import com.ponte.imessage.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    //classe statica
    companion object {
        val USER_KEY = "USER_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R .layout.activity_new_message)
        supportActionBar?.title = "Select User"
        fetchUser()
    }

    private fun fetchUser() {
        //todo dovra' prendere tutti gli utenti nel db
        val ref = FirebaseDatabase.getInstance().getReference("/users") //per prendere tutti gli utenti
        ref.addListenerForSingleValueEvent(object : ValueEventListener{   //analogo di fare un foreaceh su ogni record
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {   //per ogni figlio di users facciamo roba
                    Log.d("New Message", it.toString())

                    //da qui associamo l'oggetto dell'utente a un adapter
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid != FirebaseAuth.getInstance().uid){
                        adapter.add(UserItem(user)) //associazione
                    }
                }

                //attività al click
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem  //specifichiamo l'oggetto
                    Log.d("NewMessageActivity", "${userItem.user.username}")
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()  //per non tornare alla stessa activity
                }
                recycleViewNewMessage.adapter = adapter  //impostiamo l'adapter nella recycleView
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }) //aggiungiamo listener per ogni oggetto
    }
}

class UserItem (val user: User) : Item<ViewHolder>() {  //classe che inserisce dati dell'utente e li mette nel layout

//implementiamo Item di tipo viewHolder

    override fun bind(viewHolder: ViewHolder, position: Int) {
         viewHolder.itemView.usernameNewRowMessage.text = user.username  //abbiamo un itemView dentro a un viewHolder per lo user
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageViewRowMessage)  //immagine a fianco
    }

    override fun getLayout(): Int {  //passiamo il layout che ci interessa, ovvero quello della riga
        return R.layout.user_row_new_message
    }

}