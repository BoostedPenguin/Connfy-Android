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
    var invitedUsersIds: List<String>?,

    @SerializedName("invitedUsers")
    val invitedUsers: List<User>?,

    @SerializedName("geoLocation")
    val geoLocation: List<GeoLocation>?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("isOutlook")
    val isOutlook: Boolean?,

    @SerializedName("date")
    val date: FBDate?,
)

data class FBDate(
        var _seconds: Long,
        var _nanoseconds: Long
)

data class GeoLocation(
        var _latitude: Double,
        var _longitude: Double
)

data class MeetingRequest(
        var ownerName: String,
        var invitedUsers: List<String>,
        var geoLocation: List<GeoLocation>,
        var title: String,
        var isOutlook: Boolean
)

data class MeetingResponse(
    val data: List<Meeting>,
)

data class SingleMeetingResponse(
        var data: Meeting,
)

data class StringResponse(
        val data: String,
)