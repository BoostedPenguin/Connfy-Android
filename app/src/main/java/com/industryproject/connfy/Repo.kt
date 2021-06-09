package com.industryproject.connfy

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.networkManager.NetworkListener
import com.industryproject.connfy.networkManager.NetworkManager
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine




@Singleton
class UserRepository @Inject constructor() {

    suspend fun makeBasicRequest(listener: NetworkListener) {
        val token = getTokenResult()
        NetworkManager.getInstance()?.test(token!!.token!!, listener)
    }

    @WorkerThread
    private suspend fun getTokenResult () = suspendCoroutine<GetTokenResult?> { continuation ->
        val auth: FirebaseAuth = Firebase.auth
        auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                continuation.resume(it.result)
            } else {
                continuation.resume(null)
            }
        }
    }
}