package com.example.rpsgame.ui.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpsgame.remote.GameChoice
import com.example.rpsgame.remote.GameState
import com.example.rpsgame.ui.GameViewModel
import com.example.rpsgame.ui.view.PlayerTurnScreen
import com.example.rpsgame.ui.view.round_result.RoundResultContent
import com.example.rpsgame.ui.view.round_result.Scores
import com.example.rpsgame.ui.view.waiting_move.WaitingMoveContent
import com.example.rpsgame.ui.view.waiting_player.WaitingScreen
import com.example.rpsgame.ui.view.welcome.WelcomeScreen
import kotlinx.coroutines.launch

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val viewModel: GameViewModel = viewModel()

    val gameState by viewModel.gameState.collectAsState()

    LaunchedEffect(gameState) {
        when (gameState) {
            is GameState.WaitingForPlayers -> navController.navigate(Screen.WaitingPlayer)
            is GameState.PlayerTurn -> navController.navigate(Screen.PlayerTurn)
            is GameState.RoundOver -> navController.navigate(Screen.RoundResult)
            is GameState.GameOver -> navController.navigate(Screen.Welcome)
            null -> {
                val currentRoute = navController.currentDestination?.route
                if (currentRoute != null && !currentRoute.contains("Welcome")) {
                    navController.popBackStack(Screen.Welcome, inclusive = false)
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(500)) }
    ){
        composable<Screen.Welcome> {
            WelcomeScreen(
                onStart = {
                    viewModel.connectToGame(
                        onError = { errorMessage ->
                            scope.launch { snackbarHostState.showSnackbar(errorMessage) }
                        }
                    )
                }
            )
        }

        composable<Screen.WaitingPlayer> {
            WaitingScreen(
                onCancel = { viewModel.disconnect() }
            )
        }

        composable<Screen.PlayerTurn> {
            val myScore by viewModel.myScore.collectAsState()
            val opponentScore by viewModel.opponentScore.collectAsState()
            val selectedMove by viewModel.selectedMove.collectAsState()

            PlayerTurnScreen(
                myScore = myScore,
                opponentScore = opponentScore,
                selectedMove = selectedMove,
                onSelectMove = { choice -> viewModel.selectMove(choice) },
                onSubmit = {
                    viewModel.submitMove()
                    navController.navigate(Screen.WaitingMove)
                },
                onBackToMenu = { viewModel.disconnect() }
            )
        }

        composable<Screen.WaitingMove> {
            val myScore by viewModel.myScore.collectAsState()
            val opponentScore by viewModel.opponentScore.collectAsState()

            WaitingMoveContent(
                scores = Scores(myScore, opponentScore),
                snackbarHostState = snackbarHostState
            )
        }

        composable<Screen.RoundResult> {
            val myScore by viewModel.myScore.collectAsState()
            val opponentScore by viewModel.opponentScore.collectAsState()

            val roundOverState = gameState as? GameState.RoundOver

            if (roundOverState != null) {
                val result = roundOverState.result

                val myMove = result.playerMoves.find { it.playerId == viewModel.myPlayerId }?.choice ?: GameChoice.ROCK
                val opponentMove = result.playerMoves.find { it.playerId != viewModel.myPlayerId }?.choice ?: GameChoice.ROCK

                val iWon = result.winnerPlayerId == viewModel.myPlayerId
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

                RoundResultContent(
                    scores = Scores(myScore, opponentScore),
                    myChoice = myMove,
                    opponentChoice = opponentMove,
                    resultText = text,
                    resultColor = color,
                    onNextRound = {
                        viewModel.selectMove(GameChoice.ROCK) // Borrar después
                        navController.navigate(Screen.PlayerTurn)
                    },
                    snackbarHostState = snackbarHostState
                )
            }
        }

        composable<Screen.OpponentDisconnected> {
        }
    }
}