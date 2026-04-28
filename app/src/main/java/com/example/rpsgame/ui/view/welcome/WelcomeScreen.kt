package com.example.rpsgame.ui.view.welcome

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.rpsgame.R
import com.example.rpsgame.ui.theme.*

@Composable
fun WelcomeScreen(onStart: () -> Unit, modifier: Modifier = Modifier) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(Purple50, Pink50, Blue50)
    )

    val textBrush = Brush.linearGradient(
        colors = listOf(Purple600, Pink600)
    )

    val buttonBrush = Brush.linearGradient(
        colors = listOf(Purple600, Pink600)
    )

    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val titleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    val bounce1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce1"
    )

    val bounce2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(500)
        ),
        label = "bounce2"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.game_title),
                style = MaterialTheme.typography.headlineLarge.copy(
                    brush = textBrush
                ),
                modifier = Modifier
                    .alpha(titleAlpha)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.game_subtitle),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.raised_fist_3d_default),
                contentDescription = "Piedra",
                modifier = Modifier
                    .size(92.dp)
                    .offset(y = bounce1.dp)
            )
            Text(
                text = stringResource(id = R.string.vs_text),
                style = MaterialTheme.typography.displayMedium,
                color = Color.LightGray
            )
            Image(
                painter = painterResource(id = R.drawable.raised_hand_3d_default),
                contentDescription = "Papel",
                modifier = Modifier
                    .size(108.dp)
                    .offset(y = bounce2.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp))
                .background(buttonBrush)
                .clickable { onStart() }
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.start_game_button),
                color = Color.White,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}