package com.example.chattingappclonecoding.DataClasses

data class Message(
    // 메시지 내용
    var message: String?,
    // 보낸 유저의 유저 아이디
    var sendId: String?,
    // 받는 유저의 유저 아이디
    var receiveId: String,
) {
    constructor(): this("", "", "")
}
