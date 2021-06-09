package com.industryproject.connfy.repository

import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.api.UserHelper
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine




@Singleton
class UserRepository @Inject constructor(
    private val userHelper: UserHelper
) {

    suspend fun getUsers() = userHelper.getUsers()
    suspend fun getContacts() = userHelper.getUserContacts()
}