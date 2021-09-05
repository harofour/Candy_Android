package com.example.candy.model.data

import com.google.gson.annotations.SerializedName

data class Problem(
    @SerializedName("answer")
    val answer: String,
    @SerializedName("choiceDtoList")
    val choiceList: List<ChoiceDTO>,
    @SerializedName("content")
    val content: String,
    @SerializedName("modifiedDate")
    val modifiedDate: String,
    @SerializedName("multiple")
    val isMultipleChoice: Boolean,
    @SerializedName("multipleAnswer")
    val multipleAnswer: Int,
    @SerializedName("multipleCount")
    val multipleCount: Int,
    @SerializedName("problemId")
    val problemId: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("score")
    val score: Int,
    @SerializedName("seq")
    val problemNumber: Int
)

data class ChoiceDTO(
    @SerializedName("content")
    val option: String,
    @SerializedName("seq")
    val no: String
)

data class ProblemList(
    @SerializedName("problemResponseDtoList")
    val problem: List<Problem>
)

data class ScoredScore(
    @SerializedName("highestScoreOfAllTime")
    val score: Int
)

data class SettingProblem(
    val no: String,
    val question: String,
    val content: String,
    val problemId: Int,
    val choiceList: List<ChoiceDTO>,
    val problemAnswer: String,
    val problemScore: Int,
    var userAnswer: String? = null
)

data class SolveProblem(
    @SerializedName("problemId")
    val problemId: Int,
    @SerializedName("problemScore")
    val problemScore: Int
)

data class ProblemSolveDto(
    @SerializedName("challengeId")
    val challengeId: Int,
    @SerializedName("problemSolvedRequestDto")
    val problemSolvedRequestDto : List<SolveProblem>
)

data class TotalScore(
    @SerializedName("totalScore")
    val totalScore : Int,
    @SerializedName("userId")
    val userId : Int
)