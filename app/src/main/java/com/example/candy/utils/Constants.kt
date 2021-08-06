package com.example.candy.utils

object Constants {
    const val TAG : String = "로그"
}

enum class REQUEST_TYPE {
    LOG_IN,
    SIGN_UP,
    EMAIL_DOUBLE_CHECK
}

enum class RESPONSE_STATE{
    SUCCESS,
    FAILURE
}

object API {
    const val BASE_URL: String = "http://221.159.102.58/"
}