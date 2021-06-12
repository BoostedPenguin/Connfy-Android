package com.industryproject.connfy.repository

import com.industryproject.connfy.api.contactsApi.ContactsHelper
import com.industryproject.connfy.api.userApi.UserHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepository @Inject constructor(
        private val contactsHelper: ContactsHelper
) {
    suspend fun getContacts() = contactsHelper.getContacts()
    suspend fun addContact(contactUid: String) = contactsHelper.addContact(contactUid)
    suspend fun deleteContact(contactUid: String) = contactsHelper.deleteContact(contactUid)
}