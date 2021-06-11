package com.industryproject.connfy.repository

import com.industryproject.connfy.api.MeetingHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingRepository @Inject constructor(private val meetingHelper: MeetingHelper){

    suspend fun getMeetings() = meetingHelper.getMeetings()
    suspend fun getMeetingByUid() = meetingHelper.getMeetingByUid()
    suspend fun createMeeting() = meetingHelper.createMeeting()
    suspend fun updateMeeting() = meetingHelper.updateMeeting()
    suspend fun deleteMeeting() = meetingHelper.deleteMeeting()

}