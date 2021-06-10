package com.example.test.network.models

sealed class ViewState<out T : Any> {
    class Response<out T : Any>(val data: T): ViewState<T>()
    class Error(val text:String = "Unknown error"): ViewState<Nothing>()
}
