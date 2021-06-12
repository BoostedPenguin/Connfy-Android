package com.industryproject.connfy.ui.meeting_view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.industryproject.connfy.models.*
import com.industryproject.connfy.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingViewViewModel @Inject constructor(private val meetingRepository: MeetingRepository) : ViewModel() {
    private val _meetings = MutableLiveData<MeetingResponse>();
    val meetings: LiveData<MeetingResponse>
        get() = _meetings
    init {
        getMeetings()
    }

    private fun getMeetings() = viewModelScope.launch{
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
    private fun getMeeting(uid:String) = viewModelScope.launch{
        meetingRepository.getMeetingByUid(uid).let {

        }
    }
    private fun createMeetings(meeting: Meeting){}
    private fun updateMeeting(uid:String){}
    private fun deleteMeeting(uid:String){}
}