package com.example.rpsgame.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rpsgame.remote.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class GameViewModel : ViewModel() {

    // Cliente WebSocket de Ktor
    private val client = HttpClient(CIO) {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null

    // Generamos un ID único para este jugador de forma local (o lo puede asignar el server)
    val myPlayerId = UUID.randomUUID().toString()

    // --- Estados de la Interfaz ---

    // Estado principal del juego recibido del backend
    private val _gameState = MutableStateFlow<GameState?>(null)
    val gameState: StateFlow<GameState?> = _gameState.asStateFlow()

    private val _myScore = MutableStateFlow(0)
    val myScore: StateFlow<Int> = _myScore.asStateFlow()

    private val _opponentScore = MutableStateFlow(0)
    val opponentScore: StateFlow<Int> = _opponentScore.asStateFlow()

    private val _selectedMove = MutableStateFlow<GameChoice?>(null)
    val selectedMove: StateFlow<GameChoice?> = _selectedMove.asStateFlow()

    // --- Lógica de Red (WebSockets) ---

    fun connectToGame(onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // 10.0.2.2 es el localhost de tu PC visto desde el emulador de Android
                client.webSocket(host = "10.0.2.2", port = 8080, path = "/ws") {
                    session = this

                    // Escuchamos los mensajes entrantes del servidor de forma infinita
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val jsonMessage = frame.readText()
                            handleServerMessage(jsonMessage)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError("Error de conexión con el servidor")
                _gameState.value = null // Reseteamos si se cae
            }
        }
    }

    private fun handleServerMessage(json: String) {
        try {
            // Convertimos el JSON que manda el server al objeto GameState
            // OJO: Para que esto funcione, GameState debe tener @Serializable
            val newState = Json.decodeFromString<GameState>(json)
            _gameState.value = newState

            // Si la ronda terminó, actualizamos los marcadores
            if (newState is GameState.RoundOver) {
                updateScores(newState.result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateScores(result: RoundResultDto) {
        if (!result.isDraw && result.winnerPlayerId != null) {
            if (result.winnerPlayerId == myPlayerId) {
                _myScore.value += 1
            } else {
                _opponentScore.value += 1
            }
        }
    }

    // --- Acciones del Usuario ---

    fun selectMove(choice: GameChoice) {
        _selectedMove.value = choice
    }

    fun submitMove() {
        val choice = _selectedMove.value ?: return
        viewModelScope.launch {
            try {
                val moveDto = PlayerMoveDto(playerId = myPlayerId, choice = choice)
                val jsonMove = Json.encodeToString(moveDto)

                // Enviamos el DTO convertido a JSON por el socket
                session?.send(Frame.Text(jsonMove))

                // Limpiamos la selección
                _selectedMove.value = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            session?.close()
            session = null
            _gameState.value = null
            _myScore.value = 0
            _opponentScore.value = 0
            _selectedMove.value = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}