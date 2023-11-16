package com.example.chattingappclonecoding.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.chattingappclonecoding.R
import com.example.chattingappclonecoding.Fragments.FriendsFragment
import com.example.chattingappclonecoding.Fragments.ProfileFragment
import com.example.chattingappclonecoding.Fragments.TalkFragment
import com.example.chattingappclonecoding.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    // 레이아웃과 액티비티 연결을 위한 바인딩 변수
    private lateinit var binding: ActivityMainBinding
    // 파이어베이스 인증을 위한 변수
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 인증 초기화
        mAuth = Firebase.auth

        // 초기 액션바 제목 설정
        supportActionBar?.title = "friends"

        // 프래그먼트 전환 설정
        supportFragmentManager.beginTransaction().add(binding.frameLayout.id, FriendsFragment())
            .commit()
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.friends_menu -> {
                    supportActionBar?.title = "friends"
                    changeFragment(FriendsFragment())
                }
                R.id.talk_menu -> {
                    supportActionBar?.title = "talk"
                    changeFragment(TalkFragment())
                }
                R.id.profile_menu -> {
                    supportActionBar?.title = "profile"
                    changeFragment(ProfileFragment())
                }
            }
            true
        }
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

    // 프래그먼트 실제로 전환해주는 함수
    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }
}

