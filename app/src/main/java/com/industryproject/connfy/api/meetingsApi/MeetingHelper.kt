package com.industryproject.connfy.api.meetingsApi

import com.industryproject.connfy.models.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header

interface MeetingHelper {
    suspend fun getMeetings(): Response<MeetingResponse>
    suspend fun getMeetingByUid(uid:String): Response<SingleMeetingResponse>
    suspend fun createMeeting(request: MeetingRequest): Response<SingleMeetingResponse>
    suspend fun updateMeeting(uid:String, request: MeetingRequest): Response<SingleMeetingResponse>
    suspend fun deleteMeeting(uid:String): Response<StringResponse>
}