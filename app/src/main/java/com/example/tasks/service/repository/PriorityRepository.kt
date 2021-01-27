package com.example.tasks.service.repository

import android.content.Context
import com.example.tasks.service.models.PriorityModel
import com.example.tasks.service.repository.local.TaskDatabase
import com.example.tasks.service.repository.remote.PriorityService
import com.example.tasks.service.repository.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PriorityRepository(context: Context) {

    private val retrofit = RetrofitClient.createService(PriorityService::class.java)
    private val mDatabase = TaskDatabase.getDatabase(context)

    fun fetchAll(){
        val call : Call<List<PriorityModel>> = retrofit.fetch()

        call.enqueue(object : Callback<List<PriorityModel>>{
            override fun onFailure(call: Call<List<PriorityModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<List<PriorityModel>>,
                response: Response<List<PriorityModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let { mDatabase.priorityDao().save(it) }
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


}