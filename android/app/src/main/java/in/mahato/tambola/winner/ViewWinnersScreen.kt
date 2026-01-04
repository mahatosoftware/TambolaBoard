package `in`.mahato.tambola.winner

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.mahato.tambola.rule.entity.WinningPrizeEntity
import `in`.mahato.tambola.ui.theme.Bronze
import `in`.mahato.tambola.util.GeneralUtil

@Composable
fun ViewWinnersScreen(
    winners: List<WinningPrizeEntity>,
    onBack: () -> Unit
) {
    val focusbackButton = remember { FocusRequester() }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // -----------------------------
            // Header (Centered)
            // -----------------------------
            Text(
                text = "Winner Board",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // -----------------------------
            // Content Area (Flexible)
            // -----------------------------
            Box(modifier = Modifier.weight(1f)) {
                if (winners.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No winners yet",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 300.dp),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(winners) { item ->
                            WinnerCard(item)
                        }
                    }
                }
            }

            // -----------------------------
            // Footer (Button & Copyright)
            // -----------------------------
            Spacer(modifier = Modifier.height(16.dp))
            var backButtonFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
                    .focusRequester(focusbackButton)
                    .onFocusChanged { backButtonFocused = it.isFocused },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (backButtonFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (backButtonFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("Back")
            }

            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .navigationBarsPadding(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun WinnerCard(item: WinningPrizeEntity) {
    var focused by remember { mutableStateOf(false) }

    Card(
        onClick = { /* Handle card click if needed */ },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .widthIn(max = 400.dp)
            .focusable()
            .onFocusChanged { focused = it.isFocused },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (focused) 12.dp else 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (focused)
                MaterialTheme.colorScheme.secondaryContainer
            else
                MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.savedRule.ruleName,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Winners:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                color = Color.Yellow,
                text = "${item.winnerName}",
                fontSize = 15.sp
            )
        }
    }
}