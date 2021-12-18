package com.example.lolnamechecker.network.participantapi

data class ParticipantJson(
    val dataVersion: String,
    val matchId: String,
    val participants: List<String>
)