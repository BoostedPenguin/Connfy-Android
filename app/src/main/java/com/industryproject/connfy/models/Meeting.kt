package com.industryproject.connfy.models

import com.google.gson.annotations.SerializedName

data class Meeting(
    @SerializedName("uid")
    val uid: String?,

    @SerializedName("ownerUid")
    val ownerUid: String?,

    @SerializedName("ownerName")
    val ownerName: String?,

    @SerializedName("invitedUsersIds")
    val invitedUsersIds: ArrayList<String>?,

    @SerializedName("invitedUsers")
    val invitedUsers: ArrayList<User>?,

    @SerializedName("geoLocation")
    val geoLocation: List<GeoLocation>?,

    @SerializedName("title")
    val title: String?,
)

data class GeoLocation(
        val _latitude: Double,
        val _longitude: Double
)

data class MeetingRequest(
        var ownerName: String,
        var invitedUsers: List<String>,
        var geoLocation: List<GeoLocation>,
        var title: String
)

data class MeetingResponse(
    val data: List<Meeting>,
)

data class SingleMeetingResponse(
        val data: Meeting,
)

data class StringResponse(
        val data: String,
)