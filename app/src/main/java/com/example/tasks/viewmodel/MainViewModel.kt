package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.repository.local.SecurityPreferences

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mUserName = MutableLiveData<String>()
    var userName : LiveData<String> = mUserName

    private val mLogout = MutableLiveData<Boolean>()
    var logout : LiveData<Boolean> = mLogout

    private var sharedPreferences = SecurityPreferences(application)

    fun loadUserName(){
        val getName = sharedPreferences.get(TaskConstants.SHARED.PERSON_NAME)
        mUserName.value = getName
    }

    fun logout(){
        sharedPreferences.remove(TaskConstants.SHARED.PERSON_KEY)
        sharedPreferences.remove(TaskConstants.SHARED.PERSON_NAME)
        sharedPreferences.remove(TaskConstants.SHARED.TOKEN_KEY)
        mLogout.value = true
    }
}