package com.example.rpsgame.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpsgame.remote.GameChoice
import com.example.rpsgame.remote.GameState
import com.example.rpsgame.remote.PlayerMoveDto
import com.example.rpsgame.remote.RoundResultDto
import com.example.rpsgame.ui.view.round_result.Scores
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.close
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

data class GameUiState(
    val gameState: GameState = GameState.WaitingForPlayers,
    val scores: Scores = Scores(0, 0),
    val selectedMove: GameChoice? = null,
    val myPlayerId: String = UUID.randomUUID().toString(),
    val isConnected: Boolean = false
)

class GameViewModel : ViewModel() {

    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun connectToGame() {
        viewModelScope.launch {
            try {
                client.webSocket(host = "10.0.2.2", port = 8080, path = "/ws") {
                    session = this
                    _uiState.update { it.copy(isConnected = true) }

                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            handleServerMessage(frame.readText())
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isConnected = false) }
            }
        }
    }

    private fun handleServerMessage(json: String) {
        try {
            val newState = Json.decodeFromString<GameState>(json)

            _uiState.update { currentState ->
                val updatedScores = if (newState is GameState.RoundOver) {
                    calculateNewScores(newState.result, currentState.scores, currentState.myPlayerId)
                } else {
                    currentState.scores
                }

                currentState.copy(
                    gameState = newState,
                    scores = updatedScores
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun calculateNewScores(result: RoundResultDto, currentScores: Scores, myId: String): Scores {
        if (result.isDraw || result.winnerPlayerId == null) return currentScores

        return if (result.winnerPlayerId == myId) {
            currentScores.copy(me = currentScores.me + 1)
        } else {
            currentScores.copy(opponent = currentScores.opponent + 1)
        }
    }

    fun selectMove(choice: GameChoice) {
        _uiState.update { it.copy(selectedMove = choice) }
    }

    fun submitMove() {
        val choice = _uiState.value.selectedMove ?: return
        viewModelScope.launch {
            try {
                val moveDto = PlayerMoveDto(playerId = _uiState.value.myPlayerId, choice = choice)
                session?.send(Frame.Text(Json.encodeToString(moveDto)))
                _uiState.update { it.copy(selectedMove = null) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            session?.close()
            session = null
            _uiState.update { GameUiState() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}