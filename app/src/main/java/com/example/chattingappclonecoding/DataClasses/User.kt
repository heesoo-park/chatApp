package com.example.chattingappclonecoding.DataClasses

data class User(
    // 이름
    var name: String,
    // 이메일
    var email: String,
    // 유저 아이디
    var uId: String,
) {
    constructor(): this("", "", "")
}
