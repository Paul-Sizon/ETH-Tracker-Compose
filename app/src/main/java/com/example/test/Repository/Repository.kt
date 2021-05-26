package com.example.test.Repository


import com.example.test.network.api.Api
import com.example.test.network.models.CoinResponse
import javax.inject.Inject


class Repository @Inject constructor(private val api: Api) {
    suspend fun post(
    ): CoinResponse {
        return api.post()
    }

}
