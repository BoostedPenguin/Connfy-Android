package com.industryproject.connfy.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName



data class User(
    @SerializedName("uid")
    var uid: String?,

    @SerializedName("name")
    var name: String?,

    @SerializedName("email")
    var email: String?,

    @SerializedName("hasOutlook")
    var hasOutlook: Boolean?,
)

data class UserResponse(
    val data: List<User>,
    val status: String?,
)

data class SelfUser(
    val data: User,
)

data class ContactsResponse(
        val data: List<User>,
)