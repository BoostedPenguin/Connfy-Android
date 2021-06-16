package com.industryproject.connfy.api.contactsApi

import android.util.Log
import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.industryproject.connfy.models.ContactsResponse
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ContactsHelperImpl @Inject constructor(
        private val contactsService: ContactsService
) : ContactsHelper {


    override suspend fun getContacts(): Response<ContactsResponse> = contactsService.getUserContacts("Bearer ".plus(getTokenResult()?.token!!))
    override suspend fun addContact(contactUid: String): Response<ContactsResponse> = contactsService.addContact("Bearer ".plus(getTokenResult()?.token!!), contactUid);
    override suspend fun deleteContact(contactUid: String): Response<ContactsResponse> = contactsService.deleteContact("Bearer ".plus(getTokenResult()?.token!!), contactUid);

    @WorkerThread
    private suspend fun getTokenResult () = suspendCoroutine<GetTokenResult?> { continuation ->
        val auth: FirebaseAuth = Firebase.auth

        if (auth.currentUser == null) {
            Log.d("Firebase", "No user is logged in. Operation will seize")
        }

        auth.currentUser?.getIdToken(true)?.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("wtf", "Yes")
                Log.d("wtf", it.result?.token!!)
                continuation.resume(it.result)
            } else {
                Log.d("wtf", "No")
                continuation.resume(null)
            }
        }
    }
}