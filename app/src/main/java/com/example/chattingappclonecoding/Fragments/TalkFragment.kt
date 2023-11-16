package com.example.chattingappclonecoding.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingappclonecoding.Adapters.RoomAdapter
import com.example.chattingappclonecoding.DataClasses.Room
import com.example.chattingappclonecoding.R
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
    // 레이아웃과 액티비티 연결을 위한 바인딩 변수
    private lateinit var binding: FragmentTalkBinding
    // Room 데이터 클래스와 리사이클러뷰를 잇기 위한 어댑터 변수
    lateinit var adapter: RoomAdapter
    // 파이어베이스 인증과 실시간 데이터베이스 사용을 위한 변수들
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    // 리사이클러뷰에 넣을 Room 객체들을 모아놓는 리스트
    lateinit var roomList: ArrayList<Room>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 바인딩
        binding = FragmentTalkBinding.bind(view)
        // 인증 초기화
        mAuth = Firebase.auth
        // DB 초기화
        mDbRef = Firebase.database.reference
        // 리스트 초기화
        roomList = ArrayList()
        // 어댑터 초기화
        adapter = RoomAdapter(this.requireContext(), roomList)

        // 리사이클러뷰 세팅
        binding.roomRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.roomRecyclerView.adapter = adapter

        // 접속자의 uId
        val senderUid = mAuth.currentUser?.uid

        // 방 정보 가져오기(현재 사용자 입장)
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