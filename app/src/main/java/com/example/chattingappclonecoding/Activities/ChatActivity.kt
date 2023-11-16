package com.example.chattingappclonecoding.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chattingappclonecoding.DataClasses.Message
import com.example.chattingappclonecoding.Adapters.MessageAdapter
import com.example.chattingappclonecoding.DataClasses.Room
import com.example.chattingappclonecoding.DataClasses.User
import com.example.chattingappclonecoding.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.NonCancellable.children

class ChatActivity : AppCompatActivity() {
    // 받는 사용자의 이름
    lateinit var receiverName: String
    // 받는 사용자의 uId
    lateinit var receiverUid: String

    // 레이아웃과 액티비티 연결을 위한 바인딩 변수
    lateinit var binding: ActivityChatBinding
    // 파이어베이스 인증과 실시간 데이터베이스 사용을 위한 변수들
    lateinit var mAuth: FirebaseAuth
    lateinit var mDbRef: DatabaseReference
    // 받는 사람의 채팅방
    private lateinit var receiverRoom: String
    // 보내는 사람의 채팅방
    lateinit var senderRoom: String

    // 메시지가 저장되는 리스트
    lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 바인딩
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 채팅 띄울 리스트 초기화
        messageList = ArrayList()

        // 어댑터 초기화
        val messageAdapter: MessageAdapter = MessageAdapter(this, messageList)

        // RecyclerView 설정
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = messageAdapter

        // 넘어온 데이터 변수에 담기
        receiverName = intent.getStringExtra("name").toString()
        receiverUid = intent.getStringExtra("uId").toString()

        // 파이어베이스 인증 및 실시간 데이터베이스 초기화
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference

        // 접속자의 uId
        val senderUid = mAuth.currentUser?.uid
        var senderName: String = ""

        mDbRef.child("user").child(senderUid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    val userInfo = snapshot.getValue(User::class.java)
                    Log.d("dkj", "${userInfo}")
                    senderName = userInfo?.name!!
                }

            override fun onCancelled(error: DatabaseError) {}
        })

        // 보내는 유저의 채팅방
        senderRoom = receiverUid + senderUid

        // 받는 유저의 채팅방
        receiverRoom = senderUid + receiverUid

        // 액션바에 상대방 이름 보여주기
        supportActionBar?.title = receiverName

        // 메시지 전송 버튼 이벤트
        binding.sendBtn.setOnClickListener {
            val message = binding.messageEdit.text.toString()
            // 메시지 객체로 변환
            val messageObject = Message(message, senderUid, receiverUid)

            // 보내는 쪽 채팅방 경로에 데이터 저장
            mDbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    // 저장 성공하면 받는 쪽 채팅방 경로에도 데이터 저장
                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)
                }

            mDbRef.child("rooms").child(senderUid!!).child(receiverUid).setValue(Room(receiverName, receiverUid, message)).addOnSuccessListener {
                mDbRef.child("rooms").child(receiverUid).child(senderUid).setValue(Room(senderName, senderUid, message))
            }

            // 입력메시지 초기화
            binding.messageEdit.setText("")
        }

        // 보내는 쪽 채팅방에 경로에서 메시지 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // 먼저 리스트를 비움
                    messageList.clear()
                    // 경로 상의 데이터들을 순회하며 리스트에저장
                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }

                    // 어댑터에 데이터 변화를 알려주어 적용
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    // 에러났을 때 실행
                }

            })
    }

    private fun update(newList: ArrayList<Message>) {

    }
}