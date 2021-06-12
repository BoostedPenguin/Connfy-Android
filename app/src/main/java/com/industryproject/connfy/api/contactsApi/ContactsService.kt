package com.industryproject.connfy.api.contactsApi

import com.industryproject.connfy.models.ContactsResponse
import com.industryproject.connfy.models.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface ContactsService{

    @FormUrlEncoded
    @POST("contacts/add")
    suspend fun addContact(@Header("Authorization") header: String, @Field("contact_uid") contactUid: String ) : Response<ContactsResponse>

    @FormUrlEncoded
    @POST("contacts/delete")
    suspend fun deleteContact(@Header("Authorization") header: String, @Field("contact_uid") contactUid: String ) : Response<ContactsResponse>

    @GET("contacts")
    suspend fun getUserContacts(@Header("Authorization") header: String): Response<ContactsResponse>

}