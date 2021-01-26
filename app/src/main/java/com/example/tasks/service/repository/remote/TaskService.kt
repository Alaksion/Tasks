package com.example.tasks.service.repository.remote

import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.models.TaskModel
import retrofit2.Call
import retrofit2.http.*

interface TaskService {

    @GET(value = "Task")
    fun fetch(
        @Header(value = TaskConstants.HEADER.PERSON_KEY) personKey: String,
        @Header(value = TaskConstants.HEADER.TOKEN_KEY) tokenKey: String
    ): Call<List<TaskModel>>

    @GET(value = "Task/Next7Days")
    fun fetchNext7Days(): Call<List<TaskModel>>

    @GET(value = "Task/Overdue")
    fun fetchOverdue(): Call<List<TaskModel>>

    @GET(value = "Task/Overdue/{id}")
    fun fetchSingle(@Path(value = "id", encoded = true) taskId: Int): Call<TaskModel>

    @POST(value = "Task")
    @FormUrlEncoded
    fun create(
        @Field(value = "PriorityId") priorityId: Int,
        @Field(value = "Description") description: String,
        @Field(value = "DueDate") dueDate: String,
        @Field(value = "Complete") complete: Boolean
    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun update(
        @Field(value = "PriorityId") priorityId: Int,
        @Field(value = "Description") description: String,
        @Field(value = "DueDate") dueDate: String,
        @Field(value = "Complete") complete: Boolean
    ): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    @FormUrlEncoded
    fun markAsComplete(@Field(value = "Id") taskId: Int): Call<Boolean>

    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    @FormUrlEncoded
    fun undo(@Field(value = "Id") taskId: Int): Call<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    fun delete(@Field(value = "Id") taskId: Int): Call<Boolean>

}