package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.R
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.models.TaskModel
import com.example.tasks.service.repository.remote.RetrofitClient
import com.example.tasks.service.repository.remote.TaskService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskRepository(val context : Context){

    private val retrofit = RetrofitClient.createService(TaskService::class.java)

    fun create(task: TaskModel, listener: ApiListener<Boolean>) {
        val call: Call<Boolean> = retrofit.create(
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete
        )
        save(listener, call)
    }

    fun update(task: TaskModel, listener: ApiListener<Boolean>) {
        val call: Call<Boolean> = retrofit.update(
            task.id,
            task.priorityId,
            task.description,
            task.dueDate,
            task.complete
        )
        save(listener, call)
    }

    fun all(apiListener: ApiListener<List<TaskModel>>){
        val call : Call<List<TaskModel>> = retrofit.all()
        list(apiListener, call)
    }

    fun next7Days(apiListener: ApiListener<List<TaskModel>>){
        val call : Call<List<TaskModel>> = retrofit.nextWeek()
        list(apiListener, call)
    }

    fun overdue(apiListener: ApiListener<List<TaskModel>>){
        val call : Call<List<TaskModel>> = retrofit.expired()
        list(apiListener, call)
    }

    fun getSingle(taskId: Int, apiListener: ApiListener<TaskModel>){
        val call: Call<TaskModel> = retrofit.load(taskId)

        call.enqueue(object: Callback<TaskModel>{
            override fun onFailure(call: Call<TaskModel>, t: Throwable) {
                apiListener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<TaskModel>, response: Response<TaskModel>) {
                if(!response.isSuccessful){
                    apiListener.onError(response.errorBody()!!.toString())
                } else {
                    response.body()?.let { apiListener.onSuccess(it) }
                }
            }
        })
    }


    private fun list(apiListener: ApiListener<List<TaskModel>>, call : Call<List<TaskModel>>){
        call.enqueue(object : Callback<List<TaskModel>>{
            override fun onFailure(call: Call<List<TaskModel>>, t: Throwable) {
                apiListener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(
                call: Call<List<TaskModel>>,
                response: Response<List<TaskModel>>
            ) {
                if(!response.isSuccessful){
                    apiListener.onError(response.errorBody()!!.toString())
                } else {
                    response.body()?.let { apiListener.onSuccess(it) }
                }
            }
        })
    }

    private fun save(apiListener: ApiListener<Boolean>, call: Call<Boolean>){
        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                apiListener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (!response.isSuccessful) {
                    apiListener.onError((response.errorBody()!!.string()))
                } else {
                    response.body()?.let { apiListener.onSuccess(it) }
                }
            }
        })
    }
}