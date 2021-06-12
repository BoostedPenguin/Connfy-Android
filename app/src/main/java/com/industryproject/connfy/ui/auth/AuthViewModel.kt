package com.industryproject.connfy.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.industryproject.connfy.models.UserResponse
import com.industryproject.connfy.repository.UserRepository
import com.industryproject.connfy.utilities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _contacts =
        MutableLiveData<UserResponse>()

    val contacts : LiveData<UserResponse>
            get() = _contacts

    val onCreationComplete = MutableLiveData<Boolean>(false)

    init {
//        getEmployees()
    }


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

    fun createUserInDBGoogle(provider: String) = viewModelScope.launch {
        userRepository.createUserInDB(provider).let {
            if (it.isSuccessful){
                onCreationComplete.value = true
                Log.d("retrofit", "Added user to firestore repo")
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }

    fun createUserInDBEmail(provider: String, name: String) = viewModelScope.launch {
        userRepository.createUserInDB(provider, name).let {
            if (it.isSuccessful){
                onCreationComplete.value = true
                Log.d("retrofit", "Added user to firestore repo")
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }
}