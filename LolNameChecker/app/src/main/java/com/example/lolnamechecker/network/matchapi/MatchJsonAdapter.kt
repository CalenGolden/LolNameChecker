package com.example.lolnamechecker.network.matchapi

import com.squareup.moshi.FromJson

class MatchJsonAdapter {

    // Convert the json file into a list of match ids of type strings
    @FromJson
    fun matchList(json: List<String>): MatchListJson {
        return MatchListJson(json)
    }
}