package com.example.chattingappclonecoding.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chattingappclonecoding.DataClasses.Room
import com.example.chattingappclonecoding.R

class RoomAdapter(private val context: Context, private val roomList: ArrayList<Room>): RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.talkroom_layout, parent, false)
        return RoomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roomList.size
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentRoom = roomList[position]

        holder.roomNameText.text = currentRoom.name
        holder.recentMessageText.text = currentRoom.message
    }

    class RoomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val roomNameText: TextView = itemView.findViewById(R.id.room_name_text)
        val recentMessageText: TextView = itemView.findViewById(R.id.recent_message_text)
    }
}