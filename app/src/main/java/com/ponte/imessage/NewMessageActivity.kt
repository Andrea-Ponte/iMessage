package com.ponte.imessage

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"
        fetchUser()
    }

    private fun fetchUser() {
        //todo dovra' prendere tutti gli utenti nel db
        val ref = FirebaseDatabase.getInstance().getReference("/users") //per prendere tutti gli utenti
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                snapshot.children.forEach {
                    Log.d("New Message", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null){
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    Log.d("NewMessageActivity", "${userItem.user.username}")
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                recycleViewNewMessage.adapter = adapter
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }) //aggiungiamo listener per ogni oggetto
    }
}

class UserItem (val user:User) : Item<ViewHolder>() {


    override fun bind(viewHolder: ViewHolder, position: Int) {
         viewHolder.itemView.usernameNewRowMessage.text=user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageViewRowMessage)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

}