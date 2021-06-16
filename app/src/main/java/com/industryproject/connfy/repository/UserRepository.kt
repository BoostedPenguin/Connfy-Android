package com.industryproject.connfy.repository

import com.industryproject.connfy.api.userApi.UserHelper
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepository @Inject constructor(
    private val userHelper: UserHelper
) {

    suspend fun createUserInDB(provider: String, name: String, email: String) = userHelper.createUserInDB(provider, name, email)
    suspend fun getMainUserInfo() = userHelper.getMainUserInfo()
}