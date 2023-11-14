package com.example.chattingappclonecoding

import android.util.Log
import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {
    private var isFillName = false
    private var isFillEmail = false
    private var isFillPassword = false
    private var isFillCheckPassword = false
    private var isCorrectPassword = false

    private var isVisibleUp = false
    private var isVisibleDown = false

    fun setFlagName(flag: Boolean) {
        isFillName = flag
    }

    fun setFlagEmail(flag: Boolean) {
        isFillEmail = flag
    }

    fun setFlagPassword(flag: Boolean) {
        isFillPassword = flag
    }

    fun setFlagCheckPassword(flag: Boolean) {
        isFillCheckPassword = flag
    }

    fun setFlagCorrectPassword(flag: Boolean) {
        isCorrectPassword = flag
    }

    fun setFlagVisibleUp(flag: Boolean) {
        isVisibleUp = flag
    }

    fun setFlagVisibleDown(flag: Boolean) {
        isVisibleDown = flag
    }

    fun getFlag(): Boolean {
        return isFillName && isFillEmail && isFillPassword && isFillCheckPassword && isCorrectPassword
    }

    fun getFlagVisibleUp(): Boolean = isVisibleUp

    fun getFlagVisibleDown(): Boolean = isVisibleDown
}