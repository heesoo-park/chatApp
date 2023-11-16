package com.example.chattingappclonecoding.Fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingappclonecoding.Adapters.UserAdapter
import com.example.chattingappclonecoding.DataClasses.User
import com.example.chattingappclonecoding.R
import com.example.chattingappclonecoding.databinding.FragmentFriendsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendsFragment : Fragment(R.layout.fragment_friends) {
    // 레이아웃과 액티비티 연결을 위한 바인딩 변수
    private lateinit var binding: FragmentFriendsBinding
    // User 데이터 클래스와 리사이클러뷰를 잇기 위한 어댑터 변수
    lateinit var adapter: UserAdapter
    // 파이어베이스 인증과 실시간 데이터베이스 사용을 위한 변수들
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    // 리사이클러뷰에 넣을 User 객체들을 모아놓는 리스트
    lateinit var userList: ArrayList<User>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 바인딩
        binding = FragmentFriendsBinding.bind(view)
        // 인증 초기화
        mAuth = Firebase.auth
        // DB 초기화
        mDbRef = Firebase.database.reference
        // 리스트 초기화
        userList = ArrayList()
        // 어댑터 초기화
        adapter = UserAdapter(this.requireContext(), userList)

        // 리사이클러뷰 세팅
        binding.userRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.userRecyclerView.adapter = adapter

        // 사용자 정보 가져오기
        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            // 실시간 데이터 베이스 내에서 설정한 경로의 데이터 변동이 생겼을 때 실행
            override fun onDataChange(snapshot: DataSnapshot) {
                // 경로 상의 데이터 값들을 순회
                for (postSnapshot in snapshot.children) {
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(User::class.java)
                    // 현재 로그인한 사용자는 제외하고 리스트에 저장
                    if (mAuth.currentUser?.uid != currentUser?.uId) {
                        userList.add(currentUser!!)
                    }
                }
                // 어댑터에 리스트 값들이 변경되었음을 알려줌
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러가 난 경우에 실행
            }
        })
    }
}