package com.example.tasks.service.repository

import android.content.Context
import androidx.core.content.contentValuesOf
import com.example.tasks.R
import com.example.tasks.service.listener.ApiListener
import com.example.tasks.service.models.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(val context: Context) {

    private val retrofit = RetrofitClient.createService(PriorityService::class.java)
    private val mDatabase = TaskDatabase.getDatabase(context)

    fun fetchAll(listener : ApiListener<List<PriorityModel>>){

        if(!BaseRepository.isConnectionAvailable(context)){
            val message = context.getString(R.string.ERROR_INTERNET_CONNECTION)
            listener.onError(message)
            return
        }

        val call : Call<List<PriorityModel>> = retrofit.fetch()

        call.enqueue(object : Callback<List<PriorityModel>>{
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                listener.onError(context.getString(R.string.ERROR_UNEXPECTED))
            }

            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { listener.onSuccess(it) }
                } else {
                    listener.onError(response.errorBody().toString())
                }
            }

        })
    }

    fun list() : List<PriorityModel>{
        return mDatabase.priorityDao().list()
    }

    fun getDescription(priorityId: Int) : String{
        return mDatabase.priorityDao().getDescriptionById(priorityId)
    }

    fun save(list : List<PriorityModel>){
        mDatabase.priorityDao().clear()
        mDatabase.priorityDao().save(list)
    }


}