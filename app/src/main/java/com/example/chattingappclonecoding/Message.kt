package com.example.chattingappclonecoding

data class Message(
    // 메시지 내용
    var message: String?,
    // 보낸 유저의 유저 아이디
    var sendId: String?,
) {
    constructor(): this("", "")
}
