package com.example.rpsgame.ui.view.opponentDisconnected
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rpsgame.R
import com.example.rpsgame.ui.view.round_result.Scores

@Composable
fun OpponentDisconnectedScreen(
    scores: Scores,
    onBackToMenu: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    OpponentDisconnectedContent(
        scores = scores,
        onBackToMenu = onBackToMenu,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun OpponentDisconnectedContent(
    scores: Scores,
    onBackToMenu: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFF5F3FF), Color(0xFFFDF2F8), Color(0xFFEFF6FF))
    )

    val buttonBrush = Brush.linearGradient(
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
                .padding(24.dp)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ScoreBoardHeader(scores)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📱❌",
                    fontSize = 80.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.label_you_win),
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF4169E1),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.label_opponent_disconnected),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(buttonBrush)
                    .clickable { onBackToMenu() }
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.label_go_to_menu),
                    color = Color.White,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 18.sp
                )
            }
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
fun OpponentDisconnectedPreview() {
    MaterialTheme {
        OpponentDisconnectedContent(
            scores = Scores(3, 1),
            onBackToMenu = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}