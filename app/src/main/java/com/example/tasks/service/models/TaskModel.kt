package com.example.tasks.service.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.*

class TaskModel {

    @SerializedName(value = "Id")
    var id : Int = 0

    @SerializedName(value = "PriorityId")
    var priorityId: Int = 0

    @SerializedName(value = "Description")
    var description: String = ""

    @SerializedName(value = "DueDate")
    var dueDate: String = ""

    @SerializedName(value = "Complete")
    var complete: Boolean = false

}