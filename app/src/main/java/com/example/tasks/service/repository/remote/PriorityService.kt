package com.example.tasks.service.repository.remote

import com.example.tasks.service.models.PriorityModel
import retrofit2.Call
import retrofit2.http.GET

interface PriorityService {

    @GET(value = "Priority")
    fun fetch() : Call<List<PriorityModel>>
}