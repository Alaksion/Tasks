package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.models.TaskModel
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context : Context){

    private val retrofit = RetrofitClient.createService(TaskService::class.java)

    fun create(task: TaskModel, listener: ApiListener<Boolean>){
        val call : Call<Boolean> = retrofit.create(
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete
        )

        call.enqueue(object: Callback<Boolean>{
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(!response.isSuccessful){
                    val validationMessage =
                        Gson().fromJson(response.errorBody()!!.toString(), String::class.java)
                    listener.onError(validationMessage)
                    return
                }
                response.body()?.let { listener.onSuccess(it) }
            }

        })
    }

}