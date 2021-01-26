package com.example.tasks.service.models

import com.google.gson.annotations.SerializedName

class HeaderModel {
    @SerializedName(value = "token")
    var token : String = ""

    @SerializedName(value = "personKey")
    var personKey: String = ""

    @SerializedName(value = "name")
    var name : String = ""

}