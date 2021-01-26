package com.example.tasks.service.listener

import com.example.tasks.service.models.HeaderModel

interface ApiListener<T> {

    fun onSuccess(model: T)
    fun onError(msg : String)
}