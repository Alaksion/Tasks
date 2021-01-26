package com.example.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.listener.ValidationListener
import com.example.tasks.service.models.HeaderModel
import com.example.tasks.service.repository.PersonRepository
import com.example.tasks.service.repository.PriorityRepository
import com.example.tasks.service.repository.local.SecurityPreferences
import com.example.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val mPersonRepository = PersonRepository(application)
    private val mPriorityRepository = PriorityRepository(application)
    private val mSharedPreferences = SecurityPreferences(application)

    private val mLoginValidation = MutableLiveData<ValidationListener>()
    var loginValidation: LiveData<ValidationListener> = mLoginValidation

    private val mVerifyUserLogged = MutableLiveData<Boolean>()
    var verifyLogged = mVerifyUserLogged

    fun doLogin(email: String, password: String) {
        mPersonRepository.login(email, password, object : ApiListener<HeaderModel> {
            override fun onSuccess(model: HeaderModel) {
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_KEY, model.personKey)
                mSharedPreferences.store(TaskConstants.SHARED.TOKEN_KEY, model.token)
                mSharedPreferences.store(TaskConstants.SHARED.PERSON_NAME, model.name)

                mLoginValidation.value = ValidationListener("")

                RetrofitClient.setHeaders(model.token, model.personKey)
            }

            override fun onError(msg: String) {
                mLoginValidation.value = ValidationListener(msg)
            }
        })
    }

    fun verifyLoggedUser() {
        val tokenKey = mSharedPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = mSharedPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val logged = (tokenKey.isNotEmpty() && personKey.isNotEmpty())

        if(!logged){
            mPriorityRepository.fetchAll()
        }

        mVerifyUserLogged.value = logged
    }
}