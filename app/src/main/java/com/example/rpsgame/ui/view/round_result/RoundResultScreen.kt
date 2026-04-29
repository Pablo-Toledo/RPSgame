package com.example.rpsgame.ui.view.round_result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpsgame.R
import com.example.rpsgame.remote.GameChoice

data class Scores(
    val me: Int,
    val opponent: Int
)

@Composable
fun RoundResultScreen(
    scores: Scores,
    myChoice: GameChoice,
    opponentChoice: GameChoice,
    resultText: String,
    resultColor: Color,
    isFinalRound: Boolean,
    onNextRound: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    RoundResultContent(
        scores = scores,
        myChoice = myChoice,
        opponentChoice = opponentChoice,
        resultText = resultText,
        resultColor = resultColor,
        isFinalRound = isFinalRound,
        onNextRound = onNextRound,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun RoundResultContent(
    scores: Scores,
    myChoice: GameChoice,
    opponentChoice: GameChoice,
    resultText: String,
    resultColor: Color,
    isFinalRound: Boolean,
    onNextRound: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val bgBrush = linearGradient(
        colors = listOf(Color(0xFFF5F3FF), Color(0xFFFDF2F8), Color(0xFFEFF6FF))
    )

    val buttonBrush = linearGradient(
        colors = listOf(Color(0xFF7C3AED), Color(0xFFDB2777))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
                .padding(paddingValues)
                .padding(16.dp)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScoreBoardHeader(scores)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = resultText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = resultColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ResultMoveCard(
                        choice = myChoice,
                        label = "Tú",
                        isPlayer1 = true
                    )

                    Text(
                        text = "VS",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.LightGray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    ResultMoveCard(
                        choice = opponentChoice,
                        label = "Rival",
                        isPlayer1 = false
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(buttonBrush)
                    .clickable { onNextRound() }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val buttonText = if (isFinalRound) {
                        stringResource(id = R.string.show_results)
                    } else {
                        stringResource(id = R.string.next_round)
                    }
                    Text(
                        text = buttonText,
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ResultMoveCard(
    choice: GameChoice,
    label: String,
    isPlayer1: Boolean
) {
    val bgColor = if (isPlayer1) Color(0xFFE8EFFF) else Color(0xFFFFF0F2)
    val textColor = if (isPlayer1) Color(0xFF4169E1) else Color(0xFFDC143C)

    val icon = when (choice) {
        GameChoice.ROCK -> R.drawable.raised_fist_3d_default
        GameChoice.PAPER -> R.drawable.raised_hand_3d_default
        GameChoice.SCISSORS -> R.drawable.victory_hand_3d_default
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        modifier = Modifier.size(width = 120.dp, height = 160.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = "selection",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, color = textColor, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun ScoreBoardHeader(scores: Scores) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE8EFFF))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Yo: ${scores.me}",
                    color = Color(0xFF4169E1),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .width(1.dp)
                    .height(32.dp)
                    .background(Color.LightGray)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFF0F2))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Rival: ${scores.opponent}",
                    color = Color(0xFFDC143C),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RoundResultPreview() {
    MaterialTheme {
        RoundResultContent(
            scores = Scores(2, 1),
            myChoice = GameChoice.PAPER,
            opponentChoice = GameChoice.ROCK,
            resultText = "¡Ganaste esta ronda!",
            resultColor = Color(0xFF4169E1),
            isFinalRound = false,
            onNextRound = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}