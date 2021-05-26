package com.example.test.ViewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.test.Repository.Repository
import com.example.test.network.models.BtcResponse
import com.example.test.network.models.CoinResponse
import com.example.test.network.models.Response
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
//    val trackLiveData: MutableState<CoinResponse?> = mutableStateOf(null)



    fun post(

    ) = viewModelScope.launch(Dispatchers.Default) {
        try {
            val response =
                repository.post()
            _trackLiveData.postValue(response.rate.toString().dropLast(7))
//            _trackLiveData.value = response
        } catch (e: Exception) {
//            _trackLiveData.postValue(null)
            _trackLiveData.postValue("0.0")
            Log.i("meowViewmodelPost", e.message.toString())
        }
    }





}