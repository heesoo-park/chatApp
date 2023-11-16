package com.example.chattingappclonecoding

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingappclonecoding.Adapters.RoomAdapter
import com.example.chattingappclonecoding.DataClasses.Room
import com.example.chattingappclonecoding.DataClasses.User
import com.example.chattingappclonecoding.databinding.FragmentTalkBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class TalkFragment : Fragment(R.layout.fragment_talk) {
    private lateinit var binding: FragmentTalkBinding

    lateinit var adapter: RoomAdapter

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    lateinit var roomList: ArrayList<Room>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTalkBinding.bind(view)

        mAuth = Firebase.auth
        mDbRef = Firebase.database.reference

        roomList = ArrayList()

        // 접속자의 uId
        val senderUid = mAuth.currentUser?.uid

        adapter = RoomAdapter(this.requireContext(), roomList)

        binding.roomRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.roomRecyclerView.adapter = adapter

        mDbRef.child("rooms").child(senderUid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val roomInfo = postSnapshot.getValue(Room::class.java)
                    roomList.add(roomInfo!!)
                }
                // 어댑터에 리스트 값들이 변경되었음을 알려줌
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }
}