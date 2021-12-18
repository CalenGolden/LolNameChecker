package com.example.lolnamechecker.network

import com.squareup.moshi.Json

data class PlayerDataJson(
    @Json(name = "puuid") val playerId: String
)