package com.industryproject.connfy.api.userApi

import com.industryproject.connfy.models.SelfUser
import com.industryproject.connfy.models.UserResponse
import retrofit2.Response
import retrofit2.http.Header

interface UserHelper {
    suspend fun createUserInDB(provider: String, name: String, email: String): Response<UserResponse>
    suspend fun getMainUserInfo(): Response<SelfUser>
}