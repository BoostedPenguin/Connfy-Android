package com.industryproject.connfy.api

import com.industryproject.connfy.models.MeetingResponse
import retrofit2.Response
import retrofit2.http.*

interface MeetingService {
    @GET("meetings/")
    suspend fun getMeetings(@Header("Authorization") header: String): Response<MeetingResponse>

    @GET("meetings/:id")
    suspend fun getMeetingById(@Header("Authorization") header: String) : Response<MeetingResponse>

    @POST("meetings/add")
    suspend fun createMeeting(@Header("Authorization") header: String) : Response<MeetingResponse>

    @PUT("meetings/update/:id")
    suspend fun updateMeetingById(@Header("Authorization") header: String) : Response<MeetingResponse>

    @DELETE("meetings/delete/:id")
    suspend fun deleteMeetingById(@Header("Authorization") header: String) : Response<MeetingResponse>

}