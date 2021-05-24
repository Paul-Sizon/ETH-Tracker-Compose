package com.example.test.network.models

data class CoinResponse(
    val asset_id_base: String,
    val asset_id_quote: String,
    val rate: Double,
    val time: String
)