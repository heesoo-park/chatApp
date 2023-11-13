package com.example.chattingappclonecoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingappclonecoding.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    // 레이아웃과 액티비티 연결을 위한 바인딩 변수
    private lateinit var binding: ActivityMainBinding
    // User 데이터 클래스와 리사이클러뷰를 잇기 위한 어댑터 변수
    lateinit var adapter: UserAdapter
    // 파이어베이스 인증과 실시간 데이터베이스 사용을 위한 변수들
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    // 리사이클러뷰에 넣을 User 객체들을 모아놓는 리스트
    lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화
        mAuth = Firebase.auth
        // DB 초기화
        mDbRef = Firebase.database.reference
        // 리스트 초기화
        userList = ArrayList()
        // 어댑터 초기화
        adapter = UserAdapter(this, userList)

        // 리사이클러뷰 세팅
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
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
    // AppBar의 우측 상단에 옵션 메뉴를 세팅하는 함수
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // 옵션 메뉴의 아이템들을 선택했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 로그아웃을 누른 경우
        if (item.itemId == R.id.logout) {
            // 파이어베이스 내의 메서드를 이용하여 로그아웃
            mAuth.signOut()
            // LoginActivity로 이동
            val intent: Intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            // 현재 액티비티는 종료
            finish()
            return true
        }
        return true
    }
}