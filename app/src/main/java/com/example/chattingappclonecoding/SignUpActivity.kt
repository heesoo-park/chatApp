package com.example.chattingappclonecoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chattingappclonecoding.databinding.ActivitySignupBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    // 바인딩 객체 선언
    lateinit var binding: ActivitySignupBinding
    // 인증 객체 선언
    lateinit var mAuth: FirebaseAuth
    // 데이터베이스 객체 선언
    lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩 객체 초기화
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 인증 초기화
        mAuth = Firebase.auth

        // db 초기화
        mDbRef = Firebase.database.reference

        // 회원가입 버튼 이벤트
        binding.signUpBtn.setOnClickListener {
            // trim()은 앞뒤 공백을 잘라줌
            val name = binding.nameEdit.text.toString().trim()
            val email = binding.emailEdit.text.toString().trim()
            val password = binding.passwordEdit.text.toString().trim()

            signUp(name, email, password)
        }
    }

    // 회원가입
    private fun signUp(name: String, email: String, password: String) {
        // 이메일과 패스워드 이용하여 회원가입 진행
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { // 성공시 실행
                    // 회원가입 성공 Toast 메시지
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    // 메인 액티비티로 이동
                    val intent: Intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                    // 데이터베이스에 입력한 정보 저장
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                } else { // 실패시 실행
                    // 회원가입 실패 Toast 메시지
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }
    // 데이터베이스에 입력한 정보 저장
    private fun addUserToDatabase(name: String, email: String, uId: String) {
        // User 클래스로 묶어서 데이터베이스에 저장
        mDbRef.child("user").child(uId).setValue(User(name, email, uId))
    }
}