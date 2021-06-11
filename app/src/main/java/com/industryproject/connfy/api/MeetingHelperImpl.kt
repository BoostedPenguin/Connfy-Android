package com.industryproject.connfy.api

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.models.MeetingResponse
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MeetingHelperImpl @Inject constructor(private val meetingService: MeetingService):MeetingHelper{

    override suspend fun getMeetings(): Response<MeetingResponse> = meetingService.getMeetings("Bearer ".plus(getTokenResult()?.token!!))
    override suspend fun getMeetingByUid(): Response<MeetingResponse> = meetingService.getMeetingById("Bearer ".plus(getTokenResult()?.token!!))
    override suspend fun createMeeting(): Response<MeetingResponse> = meetingService.createMeeting("Bearer ".plus(getTokenResult()?.token!!))
    override suspend fun updateMeeting(): Response<MeetingResponse> = meetingService.updateMeetingById("Bearer ".plus(getTokenResult()?.token!!))
    override suspend fun deleteMeeting(): Response<MeetingResponse> = meetingService.deleteMeetingById("Bearer ".plus(getTokenResult()?.token!!))

    @WorkerThread
    private suspend fun getTokenResult () = suspendCoroutine<GetTokenResult?> { continuation ->
        val auth: FirebaseAuth = Firebase.auth

        if (auth.currentUser == null) {
            Log.d("Firebase", "No user is logged in. Operation will seize")
        }

        auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("meeting", "Getting Token Successful")
                Log.d("meeting", it.result?.token!!)
                continuation.resume(it.result)
            } else {
                Log.d("meeting", "Getting Token Unsuccessful")
                continuation.resume(null)
            }
        }
    }
}