package com.example.rpsgame.remote

import kotlinx.serialization.Serializable

@Serializable
enum class GameChoice {
    ROCK, PAPER, SCISSORS
}

@Serializable
data class PlayerMoveDto(
    val playerId: String,
    val choice: GameChoice
)

@Serializable
data class RoundResultDto(
    val winnerPlayerId: String?,
    val playerMoves: List<PlayerMoveDto>,
    val isDraw: Boolean = winnerPlayerId == null
)

@Serializable
data class GameResultDto(
    val roundResults: List<RoundResultDto>
)

@Serializable
sealed class GameState {
    @Serializable
    data object WaitingForPlayers : GameState()
    @Serializable
    data class PlayerTurn(val playerToMove: String) : GameState()
    @Serializable
    data class RoundOver(val result: RoundResultDto) : GameState()
    @Serializable
    data class GameOver(val result: GameResultDto) : GameState()
}