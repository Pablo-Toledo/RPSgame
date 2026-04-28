package com.example.rpsgame.ui.view.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ){
        composable<Screen.Welcome> {

        }
        composable<Screen.WaitingPlayer> {

        }
        composable<Screen.PlayerTurn> {

        }
        composable<Screen.WaitingMove> {

        }
        composable<Screen.RoundResult> {

        }
        composable<Screen.OpponentDisconnected> {

        }
    }
}
