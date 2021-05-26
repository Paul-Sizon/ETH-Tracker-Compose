package com.example.test.network.api

import com.example.test.network.models.CoinResponse
import retrofit2.http.GET
import retrofit2.http.Headers


interface Api {
    @Headers("X-CoinAPI-Key: 4F635389-87E1-431E-B0F7-0678EA8DDCCB")
    @GET(".")
    suspend fun post(

    ): CoinResponse


}


