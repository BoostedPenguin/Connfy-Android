package com.industryproject.connfy.api.contactsApi

import com.industryproject.connfy.models.ContactsResponse
import com.industryproject.connfy.models.SelfUser
import com.industryproject.connfy.models.UserResponse
import retrofit2.Response

interface ContactsHelper {
    suspend fun getContacts(): Response<ContactsResponse>
    suspend fun addContact(contactUid: String): Response<ContactsResponse>
    suspend fun deleteContact(contactUid: String): Response<ContactsResponse>
}