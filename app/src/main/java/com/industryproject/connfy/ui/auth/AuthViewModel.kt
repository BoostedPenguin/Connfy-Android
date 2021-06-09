package com.industryproject.connfy.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    val userLoggingIn = MutableLiveData<Boolean>()
}