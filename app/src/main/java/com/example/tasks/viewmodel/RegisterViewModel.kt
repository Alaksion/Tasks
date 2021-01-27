package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.models.HeaderModel
import com.example.tasks.service.models.TaskModel
import com.example.tasks.service.repository.PersonRepository

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)

    private val mFormValidation = MutableLiveData<ValidationListener>()
    var formValidation : LiveData<ValidationListener> = mFormValidation


    fun create(name: String, email: String, password: String){
        mPersonRepository.createAccount(name, email, password, false, object : ApiListener<HeaderModel>{
            override fun onSuccess(model: HeaderModel) {
                mFormValidation.value = ValidationListener()
            }

            override fun onError(msg: String) {
                mFormValidation.value = ValidationListener(msg)
            }

        })
    }
}