package com.example.lolnamechecker.network

import com.example.lolnamechecker.network.matchapi.MatchJsonAdapter
import com.example.lolnamechecker.network.matchapi.MatchListJson
import com.example.lolnamechecker.network.participantapi.GameDataJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.example.lolnamechecker.util.API_KEY
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL_PLAYER = "https://na1.api.riotgames.com"
const val BASE_URL_MATCHES = "https://americas.api.riotgames.com"

// Moshi objects for converting json data to Kotlin data objects
private val moshiPlayer = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val moshiMatch = Moshi.Builder()
    .add(MatchJsonAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val moshiParticipants = Moshi.Builder()
    //.add(ParticipantJsonAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()


/*val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logging)*/

// Use with above commented code to get logcat info of json data
// Add the line of code below to the retrofit objects before .build()
// .client(httpClient.build())

// Retrofit Objects for reading in different information from json files
private val retrofitPlayer = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiPlayer))
    .baseUrl(BASE_URL_PLAYER)
    .build()

private val retrofitMatch = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiMatch))
    .baseUrl(BASE_URL_MATCHES)
    .build()

private val retrofitParticipants = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshiParticipants))
    .baseUrl(BASE_URL_MATCHES)
    .build()

interface RiotApiService {

    // GET request for the given name's player info
    @GET("lol/summoner/v4/summoners/by-name/{summoner_name}?api_key=${API_KEY}")
    suspend fun getPlayerId(@Path("summoner_name") summonerName: String): PlayerDataJson

    // GET request for a list of match ids given a player's player id
    @GET("lol/match/v5/matches/by-puuid/{player_id}/ids?start=0&count=20&api_key=${API_KEY}")
    suspend fun getMatchlist(@Path("player_id") playerId: String): MatchListJson

    // GET request for match info given a specific match id
    @GET("lol/match/v5/matches/{match_id}?api_key=${API_KEY}")
    suspend fun getMatchParticipants(@Path("match_id") matchId: String): GameDataJson
}

object PlayerApi {
    val retrofitService: RiotApiService by lazy {
        retrofitPlayer.create(RiotApiService::class.java)
    }
}

object MatchApi {
    val retrofitService: RiotApiService by lazy {
        retrofitMatch.create(RiotApiService::class.java)
    }
}

object ParticipantApi {
    val retrofitService: RiotApiService by lazy {
        retrofitParticipants.create(RiotApiService::class.java)
    }
}