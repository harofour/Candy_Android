package com.example.candy.data

data class SignUpData(
    val email: String,
    val emailCheck: Boolean,
    val password: String,
    val parent_password: String,
    val name: String,
    val phone: String,
    val birth: Any
)
