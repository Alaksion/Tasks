package com.example.tasks.service.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "Priority")
class PriorityModel {

    @SerializedName(value = "Id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    var id : Int = 0

    @SerializedName(value = "Description")
    @ColumnInfo(name = "description")
    var description: String = ""
}