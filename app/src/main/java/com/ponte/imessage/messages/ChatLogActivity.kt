package com.ponte.imessage.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ponte.imessage.R
import com.ponte.imessage.models.ChatMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import com.ponte.imessage.models.User as User

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)


        //otteniamo l'utente con cui scriviamo e lo visulizziamo in alto

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user!!.username //titolo barra in alto
        Log.d("Chat Log Activity", "${user!!.username}")

        setupDummyData()

        send_button_chat_log.setOnClickListener {

        }

    }

    private fun performSendMessage(){
        val text = edittext_chat_log.text.toString()
        val fromId = FirebaseAuth.getInstance().uid.toString()
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user!!.uid
        if(fromId == null) return  //se non siamo loggati non mandiamo il messaggio
        val reference = FirebaseDatabase.getInstance().getReference("/message").push()
        //val chatMessage = ChatMessage(reference)

    }

    private fun setupDummyData() {
        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        recycler_view_chat_log.adapter=adapter
    }
}

class ChatFromItem : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout(): Int {

        return R.layout.chat_from_row
    }


}

class ChatToItem : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout(): Int {

        return R.layout.chat_to_row
    }

}