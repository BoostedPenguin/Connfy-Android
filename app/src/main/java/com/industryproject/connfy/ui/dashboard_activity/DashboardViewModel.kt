package com.industryproject.connfy.ui.dashboard_activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.industryproject.connfy.models.ContactsResponse
import com.industryproject.connfy.models.SelfUser
import com.industryproject.connfy.models.UserResponse
import com.industryproject.connfy.repository.ContactsRepository
import com.industryproject.connfy.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    private val _contacts =
        MutableLiveData<ContactsResponse>()

    val contacts : LiveData<ContactsResponse>
        get() = _contacts

    val thisUser = MutableLiveData<SelfUser>()


    fun getContacts()  = viewModelScope.launch {
        contactsRepository.getContacts().let {
            if (it.isSuccessful){
                _contacts.postValue(it.body())
            }else{
                Log.d("retrofit", it.message())
                Log.d("retrofit", it.code().toString())
            }
        }
    }

    fun addContact(contactUid: String) = viewModelScope.launch {
        contactsRepository.addContact(contactUid).let {
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
}