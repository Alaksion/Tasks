package com.example.tasks.service.repository

import android.content.Context
import android.text.BoringLayout
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

    fun delete(taskId: Int, listener: ApiListener<Boolean>) {

        if(!BaseRepository.isConnectionAvailable(context)){
            val message = context.getString(R.string.ERROR_INTERNET_CONNECTION)
            listener.onError(message)
            return
        }


        val call : Call<Boolean> = retrofit.delete(taskId)

        call.enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (!response.isSuccessful) {
                    listener.onError((response.errorBody()!!.string()))
                } else {
                    response.body()?.let { listener.onSuccess(it) }
                }
            }
        })
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

        if(!BaseRepository.isConnectionAvailable(context)){
            val message = context.getString(R.string.ERROR_INTERNET_CONNECTION)
            apiListener.onError(message)
            return
        }


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

    fun completeTask(taskId: Int, apiListener: ApiListener<Boolean>){
        val call : Call<Boolean> = retrofit.complete(taskId)
        changeTaskStatus(apiListener, call)
    }

    fun undoTask(taskId: Int, apiListener: ApiListener<Boolean>){
        val call : Call<Boolean> = retrofit.undo(taskId)
        changeTaskStatus(apiListener, call)
    }

    private fun changeTaskStatus(apiListener: ApiListener<Boolean>, call : Call<Boolean>){

        if(!BaseRepository.isConnectionAvailable(context)){
            val message = context.getString(R.string.ERROR_INTERNET_CONNECTION)
            apiListener.onError(message)
            return
        }

        call.enqueue(object: Callback<Boolean>{
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                apiListener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if(!response.isSuccessful){
                    apiListener.onError(response.errorBody()!!.toString())
                }
                response.body()?.let { apiListener.onSuccess(it) }
            }
        })
    }

    private fun list(apiListener: ApiListener<List<TaskModel>>, call : Call<List<TaskModel>>){

        if(!BaseRepository.isConnectionAvailable(context)){
            val message = context.getString(R.string.ERROR_INTERNET_CONNECTION)
            apiListener.onError(message)
            return
        }

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

        if(!BaseRepository.isConnectionAvailable(context)){
            val message = context.getString(R.string.ERROR_INTERNET_CONNECTION)
            apiListener.onError(message)
            return
        }

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