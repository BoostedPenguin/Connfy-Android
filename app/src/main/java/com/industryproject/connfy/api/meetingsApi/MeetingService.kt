package com.industryproject.connfy.api.meetingsApi

import com.industryproject.connfy.models.*
import retrofit2.Response
import retrofit2.http.*

interface MeetingService {
    @GET("meetings/")
    suspend fun getMeetings(@Header("Authorization") header: String): Response<MeetingResponse>

    @GET("meetings/{id}")
    suspend fun getMeetingById(@Header("Authorization") header: String, @Path("id") uid:String) : Response<SingleMeetingResponse>

    @POST("meetings/add")
    suspend fun createMeeting(@Header("Authorization") header: String,
                              @Body request: MeetingRequest) : Response<SingleMeetingResponse>

    @PUT("meetings/update/{id}")
    suspend fun updateMeetingById(@Header("Authorization") header: String,
                                  @Path("id") uid:String,
                                  @Body request: MeetingRequest
    ): Response<SingleMeetingResponse>
    @DELETE("meetings/delete/{id}")
    suspend fun deleteMeetingById(@Header("Authorization") header: String, @Path("id") uid:String) : Response<StringResponse>

}