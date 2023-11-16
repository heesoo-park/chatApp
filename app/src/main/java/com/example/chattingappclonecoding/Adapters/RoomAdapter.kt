package com.example.chattingappclonecoding.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chattingappclonecoding.Activities.ChatActivity
import com.example.chattingappclonecoding.DataClasses.Room
import com.example.chattingappclonecoding.R

class RoomAdapter(private val context: Context, private val roomList: ArrayList<Room>): RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        // talkroom_layout을 리사이클러뷰의 한 view로 설정
        val view: View = LayoutInflater.from(context).inflate(R.layout.talkroom_layout, parent, false)
        // RoomViewHolder에서 해당 레이아웃 컴포넌트에 접근할 수 있음
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    // 데이터 설정
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        // 현재 position에 위치한 방
        val currentRoom = roomList[position]

        // 화면에 데이터 보여주기
        holder.roomNameText.text = currentRoom.name
        holder.recentMessageText.text = currentRoom.message

        // 방 클릭 이벤트
        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(context, ChatActivity::class.java)
            // 이동할 액티비티로 넘길 데이터
            intent.putExtra("name", currentRoom.name)
            intent.putExtra("uId", currentRoom.uId)
            // 채팅 액티비티로 이동
            context.startActivity(intent)
        }
    }

    class RoomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // 인자로 넘겨받은 레이아웃 내의 컴포넌트에 접근
        // onBindViewHolder에서 앞에 holder를 붙이는 것으로 아래 변수 사용 가능
        val roomNameText: TextView = itemView.findViewById(R.id.room_name_text)
        val recentMessageText: TextView = itemView.findViewById(R.id.recent_message_text)
    }
}