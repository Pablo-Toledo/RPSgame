package com.example.rpsgame.ui.view.game_over

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rpsgame.R
import com.example.rpsgame.ui.theme.*
import com.example.rpsgame.ui.view.round_result.Scores

@Composable
fun GameOverScreen(
    scores: Scores,
    isVictory: Boolean,
    isDraw: Boolean,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(Purple50, Pink50, Blue50)
    )

    val buttonBrush = Brush.linearGradient(
        colors = listOf(Purple600, Pink600)
    )

    val resultColor = when {
        isDraw -> PurpleGrey40
        isVictory -> Player1Color
        else -> Pink600
    }

    val resultText = when {
        isDraw -> stringResource(id = R.string.draw_text)
        isVictory -> stringResource(id = R.string.victory_text)
        else -> stringResource(id = R.string.defeat_text)
    }

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val textAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.game_over_title),
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = resultText,
            style = MaterialTheme.typography.headlineLarge,
            color = resultColor,
            modifier = Modifier
                .alpha(textAlpha)
                .padding(horizontal = 8.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.final_score_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = PurpleGrey40
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ScoreColumn(label = "Tú", score = scores.me, color = Player1Color)

                    Text(
                        text = "-",
                        style = MaterialTheme.typography.displayMedium,
                        color = BorderGray
                    )

                    ScoreColumn(label = "Rival", score = scores.opponent, color = Pink600)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(buttonBrush)
                .clickable { onBackToMenu() }
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.back_to_menu_button),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun ScoreColumn(label: String, score: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.displayMedium,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray
        )
    }
}