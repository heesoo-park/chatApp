package com.example.chattingappclonecoding

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

// ViewHolder를 두 가지 사용하기 때문에 MessageAdapter.(만든 ViewHolder)를 쓰는 것이 아니라 RecyclerView.ViewHolder를 사용하여 상황에 맞는 ViewHolder를 매칭
class MessageAdapter(private val context: Context, private val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val receive = 1 // 받는 타입
    private val send = 2 // 보내는 타입

    // 타입에 맞는 ViewHolder로 보내서 View를 만드는 함수
    // 매개변수인 viewType은 아래의 getItemViewType 함수에서 정해져서 넣어짐
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) { // 받는 화면
            val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
            ReceiveViewHolder(view)
        } else { // 보내는 화면
            val view: View = LayoutInflater.from(context).inflate(R.layout.send, parent, false)
            SendViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // 현재 메시지
        val currentMessage = messageList[position]

        // 보내는 데이터
        if (holder.javaClass == SendViewHolder::class.java) {
            val viewHolder = holder as SendViewHolder
            viewHolder.sendMessage.text = currentMessage.message
        } else { // 받는 데이터
            val viewHolder = holder as ReceiveViewHolder
            viewHolder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        // 메시지값
        val currentMessage = messageList[position]
        // 현재 메시지를 보낸 uId와 로그인한 사용자의 uId를 비교하여 현재 메시지가 어떤 타입으로 표시되어야 하는지 결정
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)) {
            send
        } else {
            receive
        }
    }

    // 보내는 쪽
    class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage: TextView = itemView.findViewById(R.id.send_message_text)
    }

    // 받는 쪽
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.receive_message_text)
    }
}