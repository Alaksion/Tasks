package com.example.tasks.service.listener

class ValidationListener(str: String = "") {

    private var mStatus : Boolean = true
    private var mMessage : String = ""

    init{
        if(str.isNotEmpty()){
            mStatus = false
            mMessage = str
        }
    }

    fun getStatus() : Boolean{
        return this.mStatus
    }

    fun getMsg() : String{
        return this.mMessage
    }
}