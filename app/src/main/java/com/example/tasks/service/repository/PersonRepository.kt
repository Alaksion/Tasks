package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.models.HeaderModel
import com.example.tasks.service.repository.remote.PersonService
import com.example.tasks.service.repository.remote.RetrofitClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository(val context: Context) {

    private val retrofit = RetrofitClient.createService(PersonService::class.java)

    fun login(email: String, password: String, listener: ApiListener<HeaderModel>) {
        val call: Call<HeaderModel> = retrofit.login(email, password)
        call.enqueue(object : Callback<HeaderModel> {

            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validationMessage =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)

                    listener.onError(validationMessage)
                    return
                }
                response.body()?.let { listener.onSuccess(it) }
            }
        })
    }

    fun createAccount(
        name: String,
        email: String,
        password: String,
        receiveNews: Boolean,
        listener: ApiListener<HeaderModel>
    ) {
        val call: Call<HeaderModel> = retrofit.createAccount(name, email, password, receiveNews)

        call.enqueue(object : Callback<HeaderModel> {
            override fun onFailure(call: Call<HeaderModel>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<HeaderModel>, response: Response<HeaderModel>) {
                if (response.code() != TaskConstants.HTTP.SUCCESS) {
                    val validationMessage =
                        Gson().fromJson(response.errorBody()!!.string(), String::class.java)

                    listener.onError(validationMessage)
                    return
                }
                response.body()?.let { listener.onSuccess(it) }
            }
        })
    }
}