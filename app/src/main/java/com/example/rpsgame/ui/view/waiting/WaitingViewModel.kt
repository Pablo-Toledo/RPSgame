package com.example.rpsgame.ui.view.waiting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpsgame.remote.GameResultDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WaitingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WaitingUiState())
    val uiState: StateFlow<WaitingUiState> = _uiState.asStateFlow()

    fun initWaitingState(gameResult: GameResultDto, myPlayerId: String) {
        val opponentId = gameResult.roundResults
            .firstOrNull()?.playerMoves
            ?.firstOrNull { it.playerId != myPlayerId }?.playerId ?: "Rival"

        val myScore = gameResult.roundResults.count { it.winnerPlayerId == myPlayerId }
        val oppScore = gameResult.roundResults.count {
            it.winnerPlayerId != null && it.winnerPlayerId != myPlayerId
        }

        _uiState.update {
            it.copy(scores = Scores(myScore, oppScore))
        }

        simulateOpponentResponse()
    }

    private fun simulateOpponentResponse() {
        viewModelScope.launch {
            delay(2000)
            _uiState.update { it.copy(isReady = true) }
        }
    }
}

data class WaitingUiState(
    val scores: Scores = Scores(0, 0),
    val isReady: Boolean = false
)

data class Scores(
    val me: Int,
    val opponent: Int
)