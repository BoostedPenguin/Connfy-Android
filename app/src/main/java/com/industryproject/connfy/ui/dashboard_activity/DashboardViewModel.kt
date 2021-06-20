package com.industryproject.connfy.ui.dashboard_activity

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.industryproject.connfy.R
import com.industryproject.connfy.models.*
import com.industryproject.connfy.repository.ContactsRepository
import com.industryproject.connfy.repository.MeetingRepository
import com.industryproject.connfy.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
        private val userRepository: UserRepository,
        private val contactsRepository: ContactsRepository,
        private val meetingRepository: MeetingRepository,
) : ViewModel() {



    val contacts
            = MutableLiveData<ContactsResponse>()

    val selectedPersonProfile
            = MutableLiveData<User>(User(null, null, null, null))

    val currentMeeting =
            MutableLiveData<SingleMeetingResponse>()

    val creatingMeeting = Meeting(null, null, null, null, null, null, null, null, null)


    val deletedMeeting =
            MutableLiveData<StringResponse>()


    val thisUser = MutableLiveData<SelfUser>()


    fun assignSelectedPersonProfile(email: String?, name: String?, uid: String?) {
        selectedPersonProfile.value?.let {
            it.email = email
            it.name = name
            it.uid = uid
        }
    }


    fun handleContactAction() {

        val containsFriend = contacts.value?.data?.any { itUser ->
            itUser.uid == selectedPersonProfile.value?.uid
        }

        when {
            selectedPersonProfile.value?.uid == thisUser.value?.data?.uid -> {
                Log.d("Contacts","Can't take action for same user")
            }
            containsFriend == true -> {
                selectedPersonProfile.value?.uid?.let { deleteContact(it) }
            }
            else -> {
                selectedPersonProfile.value?.uid?.let { addContact(it) }
            }
        }
    }

    fun getContacts()  = viewModelScope.launch {
        contactsRepository.getContacts().let {
            if (it.isSuccessful){
                if(it.body() == null) {
                    contacts.value = ContactsResponse(emptyList())
                }
                else {
                    contacts.postValue(it.body())
                }
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }

    fun addContact(contactUid: String) = viewModelScope.launch {
        contactsRepository.addContact(contactUid).let {
            if (it.isSuccessful){
                contacts.value = it.body()
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }

    private fun deleteContact(contactUid: String) = viewModelScope.launch {
        contactsRepository.deleteContact(contactUid).let {
            if (it.isSuccessful){
                if(it.body() == null) {
                    contacts.value = ContactsResponse(emptyList())
                }
                else {
                    contacts.postValue(it.body())
                }
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }

    fun getMainUserInfo()  = viewModelScope.launch {
        userRepository.getMainUserInfo().let {
            if (it.isSuccessful){
                thisUser.postValue(it.body())
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }
    fun getMeeting(uid:String) = viewModelScope.launch{
        meetingRepository.getMeetingByUid(uid).let {
            if(it.isSuccessful){
                currentMeeting.postValue(it.body())
                Log.d("success", it.body().toString())
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createMeeting(routeList: MutableList<GeoLocation>?, date: LocalDateTime?, title: String, invitedUsers: MutableList<String>) = viewModelScope.launch{
        if (routeList != null && routeList.size > 0 && invitedUsers.size > 0) {
            val req = dateToSeconds(Date.from(date?.atZone(ZoneId.systemDefault())
                    ?.toInstant()))?.let {
                thisUser.value!!.data.name?.let { it1 ->
                        MeetingRequest(it1
                                , invitedUsers, routeList, title, false, it)
                }
            };
            if (req != null) {
                meetingRepository.createMeeting(req).let {
                    if(it.isSuccessful){
                        Log.d("success", it.body().toString())
                    }else{
                        Log.d("retrofit", it.message())
                        Log.d("retrofit", it.code().toString())
                    }
                }
            }
        }
    }
    fun updateMeeting(uid:String) = viewModelScope.launch{
        val invitedUsersIds = mutableListOf<String>("OkBrFl1snXXoPUuuyka99Ol8Rim2");
        val geoLocation = mutableListOf<GeoLocation>(GeoLocation(54.6466, 51.6479));

        val req = MeetingRequest("Azis", invitedUsersIds, geoLocation, "Title", false, 1624184972000);

        meetingRepository.updateMeeting(uid, req).let {
            if(it.isSuccessful){
                currentMeeting.postValue(it.body())
                Log.d("success", it.body().toString())
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }
    fun deleteMeeting(uid:String) = viewModelScope.launch{
        meetingRepository.deleteMeeting(uid).let {
            if(it.isSuccessful){
                Log.d("success", it.body().toString())
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateToSeconds(date: Date?): Long? {
        return try {
            val dateToSeconds: Long = date?.toInstant()?.toEpochMilli() ?: 0;
            if (dateToSeconds == 0.toLong()){
                null
            }else{
                Log.d("date: ", dateToSeconds.toString());
                dateToSeconds
            }
        }
        catch (e: Exception) {
            e.message?.let { Log.d("date: ", it) };
            null;
        }
    }
}

