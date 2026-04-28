package com.example.rpsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rpsgame.ui.navigation.Screen
import com.example.rpsgame.ui.theme.RPSgameTheme
import com.example.rpsgame.ui.view.WelcomeScreen
import com.example.rpsgame.ui.view.WaitingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )

        setContent {
            RPSgameTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.Welcome.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Screen.Welcome.route) {
                        WelcomeScreen(
                            onStart = {
                                navController.navigate(Screen.WaitingPlayer.route)
                            }
                        )
                    }

                    composable(Screen.WaitingPlayer.route) {
                        WaitingScreen(
                            onCancel = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}