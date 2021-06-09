package com.industryproject.connfy.api

import com.industryproject.connfy.models.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService{

    @GET("users")
    suspend fun getUsers(): Response<UserResponse>

    @GET("contacts")
    suspend fun getUserContacts(@Header("Authorization") header: String): Response<UserResponse>
}