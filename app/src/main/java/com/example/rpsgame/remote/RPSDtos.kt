package com.example.rpsgame.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GameChoice {
    ROCK, PAPER, SCISSORS
}

@Serializable
data class PlayerMoveDto(
    val playerId: String,
    val choice: GameChoice? = null
)

@Serializable
data class RoundResultDto(
    val winnerPlayerId: String?,
    val playerMoves: List<PlayerMoveDto>,
    val isDraw: Boolean = winnerPlayerId == null
)

@Serializable
enum class GameEndReason {
    MAX_ROUNDS_REACHED,
    OPPONENT_ABANDONED
}

@Serializable
data class GameResultDto(
    val roundResults: List<RoundResultDto>,
    val totalWinnerPlayerId: String?,
    val reason: GameEndReason
)
@Serializable
sealed class GameState {
    @Serializable @SerialName("waiting_players")
    data object WaitingForPlayers : GameState()

    @Serializable @SerialName("player_turn")
    data class PlayerTurn(val playerToMove: String) : GameState()

    @Serializable @SerialName("waiting_opponent")
    data object WaitingForOpponent : GameState()

    @Serializable @SerialName("round_over")
    data class RoundOver(val result: RoundResultDto) : GameState()

    @Serializable @SerialName("game_over")
    data class GameOver(val result: GameResultDto) : GameState()
}