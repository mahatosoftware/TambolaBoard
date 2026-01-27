package `in`.mahato.tambola.gamemode

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.mahato.tambola.game.GameActivity
import `in`.mahato.tambola.util.GeneralUtil
import androidx.compose.ui.res.stringResource
import `in`.mahato.tambola.R

@Composable
fun GameModeSelectionScreen(
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val buttonWidth = screenWidth * 0.3f

    // -----------------------------
    // Focus Requesters
    // -----------------------------
    val focusModerated = remember { FocusRequester() }
    val focusUnmoderated = remember { FocusRequester() }
    
    val focusRequesters = listOf(focusModerated, focusUnmoderated)
    var focusedIndex by remember { mutableStateOf(0) }

    // Request initial focus
    LaunchedEffect(Unit) {
        focusRequesters[focusedIndex].requestFocus()
    }

    // Move focus with looping
    fun moveFocus(up: Boolean) {
        focusedIndex = if (up) {
            (focusedIndex - 1 + focusRequesters.size) % focusRequesters.size
        } else {
            (focusedIndex + 1) % focusRequesters.size
        }
        focusRequesters[focusedIndex].requestFocus()
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.title_select_game_mode),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(48.dp))

            // -------------------------------------------
            // MODERATED BUTTON
            // -------------------------------------------
            var moderatedFocused by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    val intent = Intent(context, GameIdInputActivity::class.java)
                    context.startActivity(intent)
                    onFinish()
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp, minHeight = 50.dp)
                    .width(buttonWidth)
                    .focusRequester(focusModerated)
                    .onFocusChanged { moderatedFocused = it.isFocused }
                    .onKeyEvent {
                        if (it.type == KeyEventType.KeyDown) {
                            when (it.key) {
                                Key.DirectionDown -> { moveFocus(false); true }
                                Key.DirectionUp -> { moveFocus(true); true }
                                else -> false
                            }
                        } else false
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (moderatedFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (moderatedFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = stringResource(R.string.btn_moderated), fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // -------------------------------------------
            // UNMODERATED BUTTON
            // -------------------------------------------
            var unmoderatedFocused by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    val intent = Intent(context, `in`.mahato.tambola.rule.RuleSelectionActivity::class.java)
                    context.startActivity(intent)
                    onFinish()
                },
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp, minHeight = 50.dp)
                    .width(buttonWidth)
                    .focusRequester(focusUnmoderated)
                    .onFocusChanged { unmoderatedFocused = it.isFocused }
                    .onKeyEvent {
                        if (it.type == KeyEventType.KeyDown) {
                            when (it.key) {
                                Key.DirectionDown -> { moveFocus(false); true }
                                Key.DirectionUp -> { moveFocus(true); true }
                                else -> false
                            }
                        } else false
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (unmoderatedFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (unmoderatedFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = stringResource(R.string.btn_unmoderated), fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
