package com.industryproject.connfy.api

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.models.UserResponse
import retrofit2.Response
import retrofit2.http.Header
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserHelperImpl @Inject constructor(
    private val userService: UserService
) : UserHelper {


    override suspend fun getUsers(): Response<UserResponse> = userService.getUsers()
    override suspend fun getUserContacts(): Response<UserResponse> = userService.getUserContacts("Bearer ".plus(getTokenResult()?.token!!))
    override suspend fun createUserInDB(): Response<UserResponse> = userService.createUserInDB("Bearer ".plus(getTokenResult()?.token!!))

    @WorkerThread
    private suspend fun getTokenResult () = suspendCoroutine<GetTokenResult?> { continuation ->
        val auth: FirebaseAuth = Firebase.auth
        auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("wtf", it.result?.token!!)
                continuation.resume(it.result)
            } else {
                continuation.resume(null)
            }
        }
    }
}