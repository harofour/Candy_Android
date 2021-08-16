package com.example.candy.utils

import com.example.candy.data.User

object Constants {
    const val TAG : String = "로그"
}

object CurrentUser {
    var userInfo: User? = null
    var userToken: String? = null
}

enum class REQUEST_TYPE {
    LOG_IN,
    SIGN_UP,
    FIND_EMAIL,
    RESET_PASSWORD,
    VERIFY_EMAIL,
    SEND_AUTH,
    CHECK_AUTH
}

enum class RESPONSE_STATE{
    SUCCESS,
    FAILURE
}

object API {
    const val BASE_URL: String = "http://221.159.102.58/"
}