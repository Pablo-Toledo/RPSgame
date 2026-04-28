package com.example.rpsgame.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object WaitingPlayer : Screen("waitingPlayer")
}