package com.industryproject.connfy.models

import com.google.gson.annotations.SerializedName

data class Meeting(
    @SerializedName("uid")
    val uid: String?,

    @SerializedName("ownerUID")
    val ownerUid: String?,

    @SerializedName("ownerName")
    val ownerName: String?,

    @SerializedName("invitedUsers")
    val invitedUsers: ArrayList<User>?,

    @SerializedName("geoLocation")
    val geoLocation: List<Double>?,
)

data class MeetingResponse(
    val data: List<Meeting>,
    val status: String?,
)

data class SelfMeeting(
    val data: Meeting,
)