package com.example.candy.data

import java.io.Serializable

data class User(
    val email: String,
    val name: String,
    val id: Int,
    val birth: String,
    val loginCount: Int,
    val authority: Any
):Serializable