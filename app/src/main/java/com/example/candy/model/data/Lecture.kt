package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

data class Lecture(
    @SerializedName("lecturesUrl") val lecturesUrl: ArrayList<String>
)