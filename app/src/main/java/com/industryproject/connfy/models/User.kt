package com.industryproject.connfy.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName



data class User(
    @SerializedName("uid")
    val uid: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("hasOutlook")
    val hasOutlook: Boolean?,
)

data class UserResponse(
    val data: List<User>?,
    val status: String?,
)