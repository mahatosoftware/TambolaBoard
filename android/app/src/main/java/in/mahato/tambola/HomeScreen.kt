package `in`.mahato.tambola

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.mahato.tambola.ui.components.CopyrightFooter
import `in`.mahato.tambola.ui.theme.Gold
import `in`.mahato.tambola.ui.theme.Saffron
import androidx.compose.foundation.background

@Composable
fun HomeScreen(
    onStartNewGame: () -> Unit,
    onContinueGame: () -> Unit,
    onShowWinners: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.tambola_board_title),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Gold,
                    letterSpacing = 4.sp
                ),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            Column(
                modifier = Modifier.widthIn(max = 600.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeTile(
                        text = stringResource(id = R.string.start_new_game),
                        color = MaterialTheme.colorScheme.tertiaryContainer, // Bronze
                        textColor = Color.White,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        onClick = onStartNewGame
                    )
                    HomeTile(
                        text = stringResource(id = R.string.continue_game),
                        color = MaterialTheme.colorScheme.surfaceVariant, // Blue
                        textColor = Color.White,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        onClick = onContinueGame
                    )
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeTile(
                        text = stringResource(id = R.string.winner_board),
                        color = MaterialTheme.colorScheme.primaryContainer, // ShamockGreen
                        textColor = Color.White,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        onClick = onShowWinners
                    )
                    HomeTile(
                        text = stringResource(id = R.string.exit),
                        color = MaterialTheme.colorScheme.errorContainer,
                        textColor = Color.White,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        onClick = onExit
                    )
                }
            }
        }
        
        CopyrightFooter(
            textColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@Composable
fun HomeTile(
    text: String,
    color: Color,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    // Determine the content color to use for the card.
    // If the background is 'primary', typical M3 guidelines suggest onPrimary content.
    // But here we are manually setting containerColor.
    Card(
        onClick = onClick,
        modifier = modifier
            .onFocusChanged { isFocused = it.isFocused }
            .then(
                if (isFocused) {
                    Modifier.border(4.dp, Gold, RoundedCornerShape(24.dp))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = textColor // Use contentColor to ensure text inside is correct by default if we removed the explicit color on Text
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFocused) 16.dp else 8.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = textColor
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
