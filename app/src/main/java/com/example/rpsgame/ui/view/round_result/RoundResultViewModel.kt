package com.example.rpsgame.ui.view.round_result

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.rpsgame.remote.GameChoice
import com.example.rpsgame.remote.RoundResultDto
import com.example.rpsgame.ui.view.waiting_move.Scores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RoundResultViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RoundResultUiState())
    val uiState: StateFlow<RoundResultUiState> = _uiState.asStateFlow()

    fun initResult(
        resultDto: RoundResultDto,
        currentScores: Scores,
        myPlayerId: String
    ) {
        val myMove = resultDto.playerMoves.find { it.playerId == myPlayerId }?.choice ?: GameChoice.ROCK
        val opponentMove = resultDto.playerMoves.find { it.playerId != myPlayerId }?.choice ?: GameChoice.ROCK

        val (text, color) = when {
            resultDto.isDraw -> "¡Empate!" to Color.Gray
            resultDto.winnerPlayerId == myPlayerId -> "¡Ganaste esta ronda!" to Color(0xFF4169E1)
            else -> "¡Perdiste esta ronda!" to Color(0xFFDC143C)
        }

        _uiState.update {
            it.copy(
                scores = currentScores,
                myChoice = myMove,
                opponentChoice = opponentMove,
                resultText = text,
                resultColor = color
            )
        }
    }
}

data class RoundResultUiState(
    val scores: Scores = Scores(0, 0),
    val myChoice: GameChoice = GameChoice.ROCK,
    val opponentChoice: GameChoice = GameChoice.ROCK,
    val resultText: String = "",
    val resultColor: Color = Color.Gray
)