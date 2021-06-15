package com.industryproject.connfy.api.userApi

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.models.SelfUser
import com.industryproject.connfy.models.UserResponse
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserHelperImpl @Inject constructor(
    private val userService: UserService
) : UserHelper {


    override suspend fun createUserInDB(provider: String, name: String, email: String): Response<UserResponse> = userService.createUserInDB("Bearer ".plus(getTokenResult()?.token!!), provider, name, email)


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