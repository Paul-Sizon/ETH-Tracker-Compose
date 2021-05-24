package com.example.test.Repository


import com.example.test.network.api.Api
import com.example.test.network.models.BtcResponse
import com.example.test.network.models.CoinResponse
import com.example.test.network.models.Response
import javax.inject.Inject


class Repository @Inject constructor(private val api: Api) {
    suspend fun post(
//        appName: String
    ): CoinResponse {
        return api.post()
    }

}
