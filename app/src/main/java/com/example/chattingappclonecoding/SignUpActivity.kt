package com.example.chattingappclonecoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType.TYPE_CLASS_TEXT
import android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
import android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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

    private val signUpModel: SignUpViewModel by viewModels()

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

        // 위의 비밀번호 보여주는 토글 버튼
        binding.passwordVisibleBtn.setOnClickListener {
            if (!signUpModel.getFlagVisibleUp()) {
                binding.passwordEdit.inputType = TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or TYPE_CLASS_TEXT
                binding.passwordVisibleBtn.setImageResource(R.drawable.close_eye)
                signUpModel.setFlagVisibleUp(true)
            } else {
                binding.passwordEdit.inputType = TYPE_TEXT_VARIATION_PASSWORD or TYPE_CLASS_TEXT
                binding.passwordVisibleBtn.setImageResource(R.drawable.open_eye)
                signUpModel.setFlagVisibleUp(false)
            }

        }

        // 아래의 비밀번호 보여주는 토글 버튼
        binding.checkPasswordVisibleBtn.setOnClickListener {
            if (!signUpModel.getFlagVisibleDown()) {
                binding.checkPasswordEdit.inputType = TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or TYPE_CLASS_TEXT
                binding.checkPasswordVisibleBtn.setImageResource(R.drawable.close_eye)
                signUpModel.setFlagVisibleDown(true)
            } else {
                binding.checkPasswordEdit.inputType = TYPE_TEXT_VARIATION_PASSWORD or TYPE_CLASS_TEXT
                binding.checkPasswordVisibleBtn.setImageResource(R.drawable.open_eye)
                signUpModel.setFlagVisibleDown(false)
            }
        }

        // 이름이 채워졌는지 확인하는 Listener
        binding.nameEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    signUpModel.setFlagName(true)
                    checkFlags()
                } else {
                    signUpModel.setFlagName(false)
                }
            }
        })

        // 이메일이 채워졌는지 확인하는 Listener
        binding.emailEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                   if (!Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()) {
                       signUpModel.setFlagEmail(false)
                       binding.emailEdit.setTextColor(ContextCompat.getColor(applicationContext, R.color.red))
                       checkFlags()
                   } else {
                       signUpModel.setFlagEmail(true)
                       binding.emailEdit.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
                   }
                }
            }

        })

        // 비밀번호가 정해진 규칙에 맞춰서 입력되었는지 체크하는 Listener
        // 길이가 8보다 길고 20보다 짧은지
        // 대문자가 최소 1개 이상 들어갔는지
        // 숫자가 최소 1개 이상 들어갔는지
        binding.passwordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    if (p0.toString().length < 8) {
                        setPasswordText(View.VISIBLE, "invalid password(need more 8 characters)", R.color.red, false)
                    } else if (p0.toString().length >= 20) {
                        setPasswordText(View.VISIBLE, "invalid password(need less 20 characters)", R.color.red, false)
                    } else if (!p0.toString().contains("[A-Z]".toRegex())) {
                        setPasswordText(View.VISIBLE, "invalid password(need A~Z)", R.color.red, false)
                    } else if (!p0.toString().contains("[0-9]".toRegex())) {
                        setPasswordText(View.VISIBLE, "invalid password(need 0~9)", R.color.red, false)
                    } else {
                        setPasswordText(View.VISIBLE, "valid password", R.color.teal_700, true)
                    }
                } else {
                    setPasswordText()
                }
            }
        })

        // 위의 비밀번호와 아래 작성한 비밀번호가 동일한지 체크하는 Listener
        binding.checkPasswordEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (binding.passwordEdit.text.toString().isNotEmpty()) {
                    if (p0.toString().isNotEmpty()) {
                        if (binding.passwordEdit.text.toString() == p0.toString()) {
                            setCheckPasswordText(View.VISIBLE, "correct password", R.color.teal_700, f1 = true, f2 = true)
                        } else {
                            setCheckPasswordText(View.VISIBLE, "incorrect password", R.color.red, f1 = true, f2 = false)
                        }
                    } else {
                        setCheckPasswordText()
                    }
                }
            }
        })
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

    private fun setPasswordText(v: Int = View.GONE, s: String = "", c: Int = R.color.white, f: Boolean = false) {
        binding.passwordText.visibility = v
        binding.passwordText.text = s
        binding.passwordText.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                c
            )
        )
        signUpModel.setFlagPassword(f)
        checkFlags()
    }

    private fun setCheckPasswordText(v: Int = View.GONE, s: String = "", c: Int = R.color.white, f1: Boolean = false, f2: Boolean = false) {
        binding.checkPasswordText.visibility = v
        binding.checkPasswordText.text = s
        binding.checkPasswordText.setTextColor(ContextCompat.getColor(applicationContext, c))
        signUpModel.setFlagCheckPassword(f1)
        signUpModel.setFlagCorrectPassword(f2)
        checkFlags()
    }

    private fun checkFlags() {
        if (signUpModel.getFlag()) {
            binding.signUpBtn.isEnabled = true
            binding.signUpBtn.setBackgroundResource(R.drawable.btn_background)
        } else {
            binding.signUpBtn.isEnabled = false
            binding.signUpBtn.setBackgroundResource(R.drawable.btn_background_disabled)
        }
    }
}