package com.industryproject.connfy.ui.dashboard_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.industryproject.connfy.models.*
import com.industryproject.connfy.repository.MeetingRepository
import com.industryproject.connfy.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _contacts =
        MutableLiveData<UserResponse>()

    val currentMeeting =
            MutableLiveData<SingleMeetingResponse>()

    val deletedMeeting =
            MutableLiveData<StringResponse>()

    val contacts : LiveData<UserResponse>
        get() = _contacts

    val thisUser = MutableLiveData<SelfUser>()

    private val _meetings = MutableLiveData<MeetingResponse>();
    val meetings: LiveData<MeetingResponse>
        get() = _meetings

    fun getContacts()  = viewModelScope.launch {
        userRepository.getContacts().let {
            if (it.isSuccessful){
                _contacts.postValue(it.body())
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



    fun getMeetings() = viewModelScope.launch{
        meetingRepository.getMeetings().let {
            if (it.isSuccessful){
                _meetings.postValue(it.body())
                Log.d("success", it.body().toString())
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
    fun createMeeting() = viewModelScope.launch{
        val invitedUsersIds = mutableListOf<String>("OkBrFl1snXXoPUuuyka99Ol8Rim2", "0C2j6Vno59ZtjXUtdYhrrE1iyFz2");
        val geoLocation = mutableListOf<GeoLocation>(GeoLocation(54.6476, 51.6479));
        val req = MeetingRequest("Azis", invitedUsersIds, geoLocation, "Title");
        meetingRepository.createMeeting(req).let {
            if(it.isSuccessful){
                //currentMeeting.postValue(it.body())
                Log.d("success", it.body().toString())
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }
    fun updateMeeting(uid:String) = viewModelScope.launch{
        val invitedUsersIds = mutableListOf<String>("OkBrFl1snXXoPUuuyka99Ol8Rim2");
        val geoLocation = mutableListOf<GeoLocation>(GeoLocation(54.6466, 51.6479));
        val req = MeetingRequest("Azis", invitedUsersIds, geoLocation, "Title");
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
}