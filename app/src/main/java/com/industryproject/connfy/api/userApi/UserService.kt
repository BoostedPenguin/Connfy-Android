package com.industryproject.connfy.api.userApi

import com.industryproject.connfy.models.SelfUser
import com.industryproject.connfy.models.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface UserService{

    @FormUrlEncoded
    @POST("users/create")
    suspend fun createUserInDB(@Header("Authorization") header: String, @Field("provider") provider: String, @Field("name") name: String, @Field("email") email: String) : Response<UserResponse>

    @GET("users/")
    suspend fun getMainUserInfo(@Header("Authorization") header: String): Response<SelfUser>
}