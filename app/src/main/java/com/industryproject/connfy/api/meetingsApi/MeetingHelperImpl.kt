package com.industryproject.connfy.api.meetingsApi

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.models.*
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MeetingHelperImpl @Inject constructor(private val meetingService: MeetingService): MeetingHelper {

    override suspend fun getMeetings(): Response<MeetingResponse> = meetingService.getMeetings("Bearer ".plus(getTokenResult()?.token!!))
    override suspend fun getMeetingByUid(uid:String): Response<SingleMeetingResponse> = meetingService.getMeetingById("Bearer ".plus(getTokenResult()?.token!!), uid)
    override suspend fun createMeeting(request: MeetingRequest): Response<SingleMeetingResponse> = meetingService.createMeeting("Bearer ".plus(getTokenResult()?.token!!), request)
    override suspend fun updateMeeting(uid:String, request: MeetingRequest): Response<SingleMeetingResponse> = meetingService.updateMeetingById("Bearer ".plus(getTokenResult()?.token!!), uid, request)
    override suspend fun deleteMeeting(uid:String): Response<StringResponse> = meetingService.deleteMeetingById("Bearer ".plus(getTokenResult()?.token!!), uid)

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