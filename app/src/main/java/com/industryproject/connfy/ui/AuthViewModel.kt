package com.industryproject.connfy.ui

import androidx.lifecycle.*
import com.industryproject.connfy.UserRepository
import com.industryproject.connfy.networkManager.NetworkListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    var repo: UserRepository
) : ViewModel() {


    fun makeBasicRequest(listener: NetworkListener) = viewModelScope.launch {
        repo.makeBasicRequest(listener)
    }
}