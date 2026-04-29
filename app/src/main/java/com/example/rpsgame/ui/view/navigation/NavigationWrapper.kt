package com.example.rpsgame.ui.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpsgame.remote.GameChoice
import com.example.rpsgame.remote.GameEndReason
import com.example.rpsgame.remote.GameState
import com.example.rpsgame.ui.GameViewModel
import com.example.rpsgame.ui.view.PlayerTurnScreen
import com.example.rpsgame.ui.view.opponentDisconnected.OpponentDisconnectedScreen
import com.example.rpsgame.ui.view.round_result.RoundResultScreen
import com.example.rpsgame.ui.view.waiting_move.WaitingMoveScreen
import com.example.rpsgame.ui.view.waiting_player.WaitingScreen
import com.example.rpsgame.ui.view.welcome.WelcomeScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: GameViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.gameState) {
        val state = uiState.gameState ?: run {
            navController.navigate(Screen.Welcome) { popUpTo(0) }
            return@LaunchedEffect
        }

        fun safeNavigate(screen: Screen) {
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            val targetRoute = screen::class.qualifiedName

            if (currentRoute != targetRoute) {
                navController.navigate(screen) {
                    launchSingleTop = true
                }
            }
        }

        when (state) {
            is GameState.WaitingForPlayers -> safeNavigate(Screen.WaitingPlayer)
            is GameState.PlayerTurn -> safeNavigate(Screen.PlayerTurn)
            is GameState.WaitingForOpponent -> safeNavigate(Screen.WaitingMove)
            is GameState.RoundOver -> safeNavigate(Screen.RoundResult)
            is GameState.GameOver -> {
                if (state.result.reason == GameEndReason.OPPONENT_ABANDONED) {
                    safeNavigate(Screen.OpponentDisconnected)
                } else {
                    safeNavigate(Screen.Welcome) /** Cambiar por una pantalla de Juego Terminado */
                }
            }
            null -> {}
        }
    }

    LaunchedEffect(uiState.isConnected) {
        if (!uiState.isConnected && uiState.gameState != null && uiState.gameState !is GameState.WaitingForPlayers) {
            navController.navigate(Screen.OpponentDisconnected)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500)) }
    ) {
        composable<Screen.Welcome> {
            WelcomeScreen(
                onStart = {
                    viewModel.connectToGame()
                }
            )
        }

        composable<Screen.WaitingPlayer> {
            WaitingScreen(
                onCancel = { viewModel.resetGame() }
            )
        }

        composable<Screen.PlayerTurn> {
            PlayerTurnScreen(
                myScore = uiState.scores.me,
                opponentScore = uiState.scores.opponent,
                selectedMove = uiState.selectedMove,
                onSelectMove = { choice -> viewModel.selectMove(choice) },
                onSubmit = {
                    viewModel.submitMove()
                },
                onBackToMenu = { viewModel.resetGame() }
            )
        }

        composable<Screen.WaitingMove> {
            WaitingMoveScreen(
                scores = uiState.scores,
                snackbarHostState = snackbarHostState
            )
        }

        composable<Screen.RoundResult> {
            val roundOverState = uiState.gameState as? GameState.RoundOver

            if (roundOverState != null) {
                val result = roundOverState.result
                val myMove = result.playerMoves.find { it.playerId == uiState.myPlayerId }?.choice ?: GameChoice.ROCK
                val opponentMove = result.playerMoves.find { it.playerId != uiState.myPlayerId }?.choice ?: GameChoice.ROCK

                val iWon = result.winnerPlayerId == uiState.myPlayerId
                val isDraw = result.isDraw

                val text = when {
                    isDraw -> "¡Empate!"
                    iWon -> "¡Ganaste esta ronda!"
                    else -> "¡Perdiste esta ronda!"
                }
                val color = when {
                    isDraw -> Color.Gray
                    iWon -> Color(0xFF4169E1)
                    else -> Color(0xFFDC143C)
                }

                RoundResultScreen(
                    scores = uiState.scores,
                    myChoice = myMove,
                    opponentChoice = opponentMove,
                    resultText = text,
                    resultColor = color,
                    onNextRound = {
                        navController.navigate(Screen.PlayerTurn)
                    },
                    snackbarHostState = snackbarHostState
                )
            }
        }

        composable<Screen.OpponentDisconnected> {
            OpponentDisconnectedScreen(
                scores = uiState.scores,
                onBackToMenu = {
                    viewModel.resetGame()
                    navController.navigate(Screen.Welcome)
                },
                snackbarHostState = snackbarHostState
            )
        }
    }
}