package com.example.candy.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CandyResponse(
    @SerializedName("response")
    val candy: Candy
)

data class CandyResponse2(
    @SerializedName("response")
    val candy: Candy2
)

data class Candy(
    @SerializedName("candyAmount")
    val candy: String
)

data class Candy2(
    @SerializedName("candyAmount")
    val candy: Int
)

data class ChargeCandyResponse(
    @SerializedName("error")
    val error: Error?,
    @SerializedName("response")
    val response: Candy,
    @SerializedName("success")
    val success: Boolean
)

data class History(
    @SerializedName("amount")
    var amount: String,
    @SerializedName("createDate")
    var createDate: String,
    @SerializedName("eventType")
    var eventType: String,
    @SerializedName("id")
    val id: String
)

data class HistoryResponse(
    @SerializedName("error")
    val error: Error?,
    @SerializedName("response")
    val response: ArrayList<History>,
    @SerializedName("success")
    val success: Boolean
)

data class CandyAssignResponse(
    @SerializedName("success")
    val isAssignSuccess: Boolean
)

data class CandyAssignBody(
    @SerializedName("candyAmount")
    val candyAmount: Int,
    @SerializedName("challengeId")
    val challengeId: Int,
    @SerializedName("parentPassword")
    val parrentPasword: String
)

