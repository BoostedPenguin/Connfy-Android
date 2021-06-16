package com.industryproject.connfy.repository

import com.industryproject.connfy.api.meetingsApi.MeetingHelper
import com.industryproject.connfy.models.MeetingRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingRepository @Inject constructor(private val meetingHelper: MeetingHelper){

    suspend fun getMeetings() = meetingHelper.getMeetings()
    suspend fun getMeetingByUid(uid: String) = meetingHelper.getMeetingByUid(uid)
    suspend fun createMeeting(request: MeetingRequest) = meetingHelper.createMeeting(request)
    suspend fun updateMeeting(uid: String, request: MeetingRequest) = meetingHelper.updateMeeting(uid, request)
    suspend fun deleteMeeting(uid: String) = meetingHelper.deleteMeeting(uid)

}