package com.example.lolnamechecker.overview

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.lolnamechecker.R
import com.example.lolnamechecker.network.*
import com.example.lolnamechecker.network.participantapi.GameDataJson
import kotlinx.coroutines.launch
import retrofit2.HttpException

enum class RiotApiStatus { SEARCHING, ERROR, DONE }

class OverviewViewModel(application: Application) : AndroidViewModel(application) {

    // BAD PRACTICE!!!
    private val context = getApplication<Application>().applicationContext

    // Live Data variables for accessing the inputted and calculated variables
    private val _status = MutableLiveData<RiotApiStatus>()
    val status: LiveData<RiotApiStatus> = _status

    private val _count = MutableLiveData<Int>(-1)
    val count: LiveData<Int> = _count

    private val _playerName = MutableLiveData<String>("Player X")
    val playerName: LiveData<String> = _playerName

    private val _searchName = MutableLiveData<String>("Player Y")
    val searchName: LiveData<String> = _searchName

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> = _errorMsg

    val numberMatches = "20"

    private lateinit var playerId: String
    private lateinit var checkedId: String


    fun getNumberTimes(summonerName: String, checkName: String) {

        _playerName.value = summonerName
        _searchName.value = checkName
        _status.value = RiotApiStatus.SEARCHING

        // Start a coroutine to make calls to Riot's API
        viewModelScope.launch {

            try {
                val primaryPlayerJson = PlayerApi.retrofitService.getPlayerId(summonerName)
                playerId = primaryPlayerJson.playerId

                val checkedPlayerJson = PlayerApi.retrofitService.getPlayerId(checkName)
                checkedId = checkedPlayerJson.playerId

                // returns a list of match ids up to {numberMatches} given the player's id
                val matchJson = MatchApi.retrofitService.getMatchlist(playerId)
                var gameDataJson: GameDataJson
                var currentCount = 0
                for (matchId in matchJson.matches) {
                    //returns the match data of a specific match given the specific match id
                    gameDataJson = ParticipantApi.retrofitService.getMatchParticipants(matchId)
                    currentCount += checkNames(gameDataJson.metadata.participants)
                }
                _count.value = currentCount
                _status.value = RiotApiStatus.DONE
            } catch (e: HttpException) {
                _status.value = RiotApiStatus.ERROR
                _count.value = -1
                _errorMsg.value = e.message
                when (e.message.toString()) {
                    "HTTP 404 Not Found" -> {
                        val toast = Toast.makeText(context, "One of the given Summoner Names is not found.", Toast.LENGTH_LONG)
                        toast.show()
                    }
                    "HTTP 429 Too Many Requests" -> {
                        val toast = Toast.makeText(context, "Too many requests, please try again in a minute.", Toast.LENGTH_LONG)
                        toast.show()
                    }
                }
            }

        }

    }

    // Run through the list of names and return the number of occurrences of of the searched player ID
    private fun checkNames(players: List<String>): Int {
        var count = 0
        for (name in players) {
            if (name == checkedId) {
                count++
            }
        }
        return count
    }

}