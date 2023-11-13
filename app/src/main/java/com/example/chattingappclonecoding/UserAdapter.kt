package com.example.chattingappclonecoding

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val context: Context, private val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    // 화면 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // user_layout을 리사이클러뷰의 한 view로 설정
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        // UserViewHolder에서 해당 레이아웃 컴포넌트에 접근할 수 있음
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    // 데이터 설정
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // 현재 position에 위치한 유저
        val currentUser = userList[position]

        // 화면에 데이터 보여주기
        holder.nameText.text = currentUser.name

        // 유저 클릭 이벤트
        holder.itemView.setOnClickListener {
            // 채팅 액티비티로 이동
            val intent: Intent = Intent(context, ChatActivity::class.java)

            // 이동할 액티비티로 넘길 데이터
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uId", currentUser.uId)

            context.startActivity(intent)
        }
    }

    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // 인자로 넘겨받은 레이아웃 내의 컴포넌트에 접근
        // onBindViewHolder에서 앞에 holder를 붙이는 것으로 아래 변수 사용 가능
        val nameText: TextView = itemView.findViewById(R.id.name_text)
    }
}