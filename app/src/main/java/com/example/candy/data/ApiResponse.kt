package com.example.candy.data

import java.io.Serializable

data class ApiResponse(
    val error: Any,
    val response: Response,
    val success: Boolean
)

