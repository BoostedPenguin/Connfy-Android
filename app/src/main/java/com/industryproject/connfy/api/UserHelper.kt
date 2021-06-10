package com.industryproject.connfy.api

import com.industryproject.connfy.models.UserResponse
import retrofit2.Response
import retrofit2.http.Header

interface UserHelper {
    suspend fun getUsers(): Response<UserResponse>
    suspend fun getUserContacts(): Response<UserResponse>
    suspend fun createUserInDB(): Response<UserResponse>
}