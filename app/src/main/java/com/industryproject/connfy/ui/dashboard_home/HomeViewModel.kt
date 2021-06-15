package com.industryproject.connfy.ui.dashboard_home

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.industryproject.connfy.models.MeetingResponse
import com.industryproject.connfy.repository.MeetingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
        private val meetingRepository: MeetingRepository,
) : ViewModel() {

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("dd/MM/yyyy")


    val meetings =
            MutableLiveData<MeetingResponse>();


    fun getMeetings() = viewModelScope.launch{
        meetingRepository.getMeetings().let {
            if (it.isSuccessful){
                if(it.body() == null) {
                    meetings.value = MeetingResponse(emptyList())
                }
                else {
                    val futureMeetings = it.body()!!.data.filter { meeting ->
                        !passedMeeting(meeting.date!!._seconds)
                    }

                    meetings.postValue(MeetingResponse(futureMeetings))
                }
                Log.d("success", it.body().toString())
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }

    private fun passedMeeting(seconds: Long) : Boolean {
        val dateString: String = sdf.format(Date(seconds * 1000L))

        val strDate: Date = sdf.parse(dateString)
        if (System.currentTimeMillis() > strDate.time) {
            return true
        }
        return false
    }
}