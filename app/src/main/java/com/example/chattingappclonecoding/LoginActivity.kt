package com.example.chattingappclonecoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.chattingappclonecoding.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    // 바인딩 객체 선언
    lateinit var binding: ActivityLoginBinding
    // Firebase 인증 객체 선언
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩 객체 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화
        mAuth = Firebase.auth

        supportActionBar?.hide()

        // 로그인 버튼 이벤트
        binding.loginBtn.setOnClickListener {
            // trim()은 앞뒤 공백을 잘라줌
            val email: String = binding.emailEdit.text.toString().trim()
            val password: String = binding.passwordEdit.text.toString().trim()

            login(email, password)
        }

        // 회원가입 버튼 이벤트
        binding.signUpBtn.setOnClickListener {
            // 회원가입 액티비티로 이동
            val intent: Intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // 로그인
    private fun login(email: String, password: String) {
        // 이메일과 패스워드 이용하여 로그인(인증) 진행
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 성공 시 실행
                    // 메인 액티비티로 이동
                    val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    // 로그인 성공 Toast 메시지
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    // 로그인 액티비티는 종료
                    finish()
                } else { // 실패 시 실행
                    // 로그인 실패 Toast 메시지
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    // 로그인 실패 원인 알 수 있는 Toast 메시지
                    Log.d("Login", "Error: ${task.exception}")
                }
            }
    }
}