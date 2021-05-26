package com.example.test.ViewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.test.Repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    val _trackLiveData: MutableLiveData<String> = MutableLiveData()
    val trackLiveData: LiveData<String> = _trackLiveData

    fun post(

    ) = viewModelScope.launch(Dispatchers.Default) {
        try {
            val response =
                repository.post()
            _trackLiveData.postValue(response.rate.toString().dropLast(7))
        } catch (e: Exception) {
            _trackLiveData.postValue("0.0")
            Log.i("meowViewmodelPost", e.message.toString())
        }
    }





}