package com.example.rpsgame.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rpsgame.R
import com.example.rpsgame.remote.GameChoice
import com.example.rpsgame.ui.theme.*

@Composable
fun PlayerTurnScreen(
    currentRound: Int,
    myScore: Int,
    opponentScore: Int,
    selectedMove: GameChoice?,
    onSelectMove: (GameChoice) -> Unit,
    onSubmit: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(Purple50, Pink50, Blue50)
    )

    val submitBrush = Brush.linearGradient(
        colors = listOf(Purple600, Pink600)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .systemBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Purple50)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.round_indicator, currentRound),
                        color = Purple600,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(32.dp)
                        .background(BorderGray)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Player1Bg)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.score_me, myScore),
                        color = Player1Color,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(32.dp)
                        .background(BorderGray)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Player2Bg)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.score_opponent, opponentScore),
                        color = Player2Color,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.turn_title),
                style = MaterialTheme.typography.titleMedium,
                color = Player1Color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = R.string.turn_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MoveCard(
                gameChoice = GameChoice.ROCK,
                label = stringResource(id = R.string.move_rock),
                iconResId = R.drawable.raised_fist_3d_default,
                isSelected = selectedMove == GameChoice.ROCK,
                onClick = { onSelectMove(GameChoice.ROCK) }
            )
            MoveCard(
                gameChoice = GameChoice.PAPER,
                label = stringResource(id = R.string.move_paper),
                iconResId = R.drawable.raised_hand_3d_default,
                isSelected = selectedMove == GameChoice.PAPER,
                onClick = { onSelectMove(GameChoice.PAPER) }
            )
            MoveCard(
                gameChoice = GameChoice.SCISSORS,
                label = stringResource(id = R.string.move_scissors),
                iconResId = R.drawable.victory_hand_3d_default,
                isSelected = selectedMove == GameChoice.SCISSORS,
                onClick = { onSelectMove(GameChoice.SCISSORS) }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onBackToMenu() }
                    .border(2.dp, BorderGray, RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.exit_button),
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (selectedMove != null) submitBrush
                        else Brush.linearGradient(listOf(DisabledBg, DisabledBg))
                    )
                    .clickable(enabled = selectedMove != null) { onSubmit() }
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.submit_button),
                    color = if (selectedMove != null) Color.White else DisabledText,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun MoveCard(
    gameChoice: GameChoice,
    label: String,
    iconResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(4.dp, Player1Color, RoundedCornerShape(20.dp))
                else Modifier
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = label,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}