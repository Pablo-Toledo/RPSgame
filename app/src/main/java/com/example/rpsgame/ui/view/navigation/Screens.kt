package com.example.rpsgame.ui.view.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(){
    @Serializable object Welcome : Screen()
    @Serializable object PlayerTurn : Screen()
    @Serializable object Waiting : Screen()
    @Serializable object RoundResult : Screen()
    @Serializable object OpponentDisconnected : Screen ()
}