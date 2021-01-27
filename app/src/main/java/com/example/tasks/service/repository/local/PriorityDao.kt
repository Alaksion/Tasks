package com.example.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tasks.service.models.PriorityModel

@Dao
interface PriorityDao {

    @Insert
    fun save(priorityList: List<PriorityModel>) : List<Long>

    @Query(value = "Select * From Priority")
    fun list(): List<PriorityModel>

    @Query(value = "Delete from Priority")
    fun clear()

    @Query(value = "Select description From priority Where id = :priorityId")
    fun getDescriptionById(priorityId: Int) : String
}