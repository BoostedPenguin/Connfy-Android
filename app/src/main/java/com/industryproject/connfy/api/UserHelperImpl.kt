package com.industryproject.connfy.api

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import com.industryproject.connfy.models.SelfUser
import com.industryproject.connfy.models.UserResponse
import okhttp3.ResponseBody
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
    override suspend fun createUserInDB(provider: String): Response<UserResponse> = userService.createUserInDB("Bearer ".plus(getTokenResult()?.token!!), provider)
    override suspend fun createUserInDB(provider: String, name: String): Response<UserResponse> = userService.createUserInDB("Bearer ".plus(getTokenResult()?.token!!), provider, name)


    override suspend fun getMainUserInfo(): Response<SelfUser> = userService.getMainUserInfo("Bearer ".plus(getTokenResult()?.token!!))

    @WorkerThread
    private suspend fun getTokenResult () = suspendCoroutine<GetTokenResult?> { continuation ->
        val auth: FirebaseAuth = Firebase.auth

        if (auth.currentUser == null) {
            Log.d("Firebase", "No user is logged in. Operation will seize")
        }

        auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("wtf", "Yes")
                Log.d("wtf", it.result?.token!!)
                continuation.resume(it.result)
            } else {
                Log.d("wtf", "No")
                continuation.resume(null)
            }
        }
    }
}