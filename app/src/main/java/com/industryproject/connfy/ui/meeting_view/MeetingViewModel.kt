package com.industryproject.connfy.ui.meeting_view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.industryproject.connfy.models.*
import com.industryproject.connfy.repository.ContactsRepository
import com.industryproject.connfy.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MeetingViewModel @Inject constructor(
        private val meetingRepository: MeetingRepository,
) : ViewModel() {

    val currentMeeting =
            MutableLiveData<SingleMeetingResponse>()


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

    fun updateMeeting(uid:String) = viewModelScope.launch{
        val invitedUsersIds = mutableListOf<String>("OkBrFl1snXXoPUuuyka99Ol8Rim2");
        val geoLocation = mutableListOf<GeoLocation>(GeoLocation(54.6466, 51.6479));

        val req = MeetingRequest("Azis", invitedUsersIds, geoLocation, "Title", false);

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