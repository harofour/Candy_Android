package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

data class Problem(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("choiceDtoList")
    val choiceList : List<ChoiceDTO>,
    @SerializedName("content")
    val content : String,
    @SerializedName("modifiedDate")
    val modifiedDate: String,
    @SerializedName("multiple")
    val isMultipleChoice : Boolean,
    @SerializedName("multipleAnswer")
    val multipleAnswer: Int,
    @SerializedName("multipleCount")
    val multipleCount: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("seq")
    val ProblemNumber : Int
)

data class ChoiceDTO(
    @SerializedName("content")
    val option : String,
    @SerializedName("seq")
    val no : String
)

data class ProblemList(
    @SerializedName("problemResponseDtoList")
    val problem: List<Problem>
)

data class SettingProblem(
    val no : String,
    val question : String,
    val content : String,
    val answer : String,
    val choiceList : List<ChoiceDTO>
)