package com.example.chattingappclonecoding.DataClasses

data class Room(
    // 받는 유저 이름
    var name: String,
    // 받는 유저 아이디
    var uId: String,
    // 최근 메시지
    var message: String,
) {
    constructor(): this("", "", "")
}
