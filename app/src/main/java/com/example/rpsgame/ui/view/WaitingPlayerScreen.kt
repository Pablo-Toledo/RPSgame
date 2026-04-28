package com.example.rpsgame.ui.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rpsgame.R
import com.example.rpsgame.ui.theme.*

@Composable
fun WaitingScreen(onCancel: () -> Unit, modifier: Modifier = Modifier) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(Purple50, Pink50, Blue50)
    )

    val textBrush = Brush.linearGradient(
        colors = listOf(Purple600, Pink600)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "waiting_pulse")
    val textAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        CircularProgressIndicator(
            color = Purple600,
            modifier = Modifier.size(80.dp),
            strokeWidth = 6.dp
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(id = R.string.waiting_title),
            style = MaterialTheme.typography.headlineLarge.copy(
                brush = textBrush
            ),
            modifier = Modifier
                .alpha(textAlpha)
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .clickable { onCancel() }
                .border(
                    width = 2.dp,
                    brush = textBrush,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.cancel_button),
                color = Purple600,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}