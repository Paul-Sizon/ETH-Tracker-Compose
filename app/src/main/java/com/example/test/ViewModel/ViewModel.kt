package com.example.test.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test.Repository.Repository
import com.example.test.ext.round
import com.example.test.network.models.CoinResponse
import com.example.test.network.models.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _trackLiveData: MutableLiveData<ViewState<Cost>> = MutableLiveData()
    private var oldPrice = 0.0
    val trackLiveData: LiveData<ViewState<Cost>>
        get() = _trackLiveData
    private var response: CoinResponse = CoinResponse("", "", 0.0, "")

    fun post(
    ) = viewModelScope.launch(Dispatchers.Default) {
        Log.v("TAG", "post")
        try {
            oldPrice = response.rate

            response = repository.post()
            val newPrice = response.rate

            val price = Cost(oldPrice.round(7), newPrice.round(7))
            _trackLiveData.postValue(ViewState.Response(price))
            Log.v("TAG VM", "old: $oldPrice, new: $newPrice")
        } catch (e: Exception) {
            _trackLiveData.postValue(ViewState.Error())
            Log.v("TAG: viewmodelPost", e.message.toString())
        }
    }
}

data class Cost(var old: Double, var new: Double)