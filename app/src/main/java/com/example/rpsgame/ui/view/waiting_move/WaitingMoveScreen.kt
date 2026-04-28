package com.example.rpsgame.ui.view.waiting_move

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rpsgame.remote.GameResultDto
import com.example.rpsgame.R

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun WaitingMoveMoveScreenPreview() {
    val mockScores = Scores(me = 3, opponent = 2)
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme {
        WaitingMoveContent(
            scores = mockScores,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun WaitingMoveScreen(
    gameResult: GameResultDto,
    myPlayerId: String,
    snackbarHostState: SnackbarHostState,
    onResultReady: () -> Unit,
    viewModel: WaitingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initWaitingState(gameResult, myPlayerId)
    }

    LaunchedEffect(uiState.isReady) {
        if (uiState.isReady) onResultReady()
    }

    WaitingMoveContent(
        scores = uiState.scores,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun WaitingMoveContent(
    scores: Scores,
    snackbarHostState: SnackbarHostState
) {
    val backgroundGradient = Brush.linearGradient(
        colors = listOf(Color(0xFFFFF7ED), Color(0xFFFDF2F8), Color(0xFFFEF2F2))
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScoreBoardHeader(scores)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WaitingSpinner()
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = stringResource(id = R.string.waiting_move),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFFDC143C),
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = stringResource(id = R.string.submitted_move),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ScoreBoardHeader(scores: Scores) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
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

@Composable
fun WaitingSpinner() {
    val infiniteTransition = rememberInfiniteTransition(label = "waiting")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(modifier = Modifier
        .size(80.dp)
        .rotate(angle)
    ) {
        drawArc(
            color = Color(0xFFF97316),
            startAngle = 0f,
            sweepAngle = 280f,
            useCenter = false,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}