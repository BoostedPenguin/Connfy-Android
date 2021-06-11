package com.industryproject.connfy.api

import com.industryproject.connfy.models.MeetingResponse
import retrofit2.Response

interface MeetingHelper {
    suspend fun getMeetings(): Response<MeetingResponse>
    suspend fun getMeetingByUid(): Response<MeetingResponse>
    suspend fun createMeeting(): Response<MeetingResponse>
    suspend fun updateMeeting(): Response<MeetingResponse>
    suspend fun deleteMeeting(): Response<MeetingResponse>
}