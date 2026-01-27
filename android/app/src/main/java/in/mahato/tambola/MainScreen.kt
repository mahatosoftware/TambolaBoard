package `in`.mahato.tambola

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.mahato.tambola.util.GeneralUtil
import androidx.compose.ui.res.stringResource
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSelectGameRule: () -> Unit,
    onNewGame: () -> Unit,
    onContinue: () -> Unit,
    onViewWinners : () -> Unit,
    onExit: () -> Unit
) {
    val year = Calendar.getInstance().get(Calendar.YEAR)

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val buttonWidth = screenWidth * 0.3f

    // -----------------------------
    // Focus Requesters for 4 buttons
    // -----------------------------

    val focusNew = remember { FocusRequester() }
    val focusContinue = remember { FocusRequester() }
    val focusViewWinners = remember { FocusRequester() }
    val focusExit = remember { FocusRequester() }

    val focusRequesters = listOf(focusNew, focusContinue,focusViewWinners, focusExit)
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

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.main_welcome_message),
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))



            //-------------------------------------------
            // START NEW GAME
            //-------------------------------------------
            var newGameFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onNewGame,
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp, minHeight = 50.dp)
                    .width(buttonWidth)
                    .focusRequester(focusNew)
                    .onFocusChanged { newGameFocused = it.isFocused }
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
                    containerColor = if (newGameFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (newGameFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = stringResource(R.string.btn_start_new_game))
            }

            Spacer(modifier = Modifier.height(16.dp))

            //-------------------------------------------
            // CONTINUE LAST GAME
            //-------------------------------------------
            var continueFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp, minHeight = 50.dp)
                    .width(buttonWidth)
                    .focusRequester(focusContinue)
                    .onFocusChanged { continueFocused = it.isFocused }
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
                    containerColor = if (continueFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (continueFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = stringResource(R.string.btn_continue_last_game))
            }

            Spacer(modifier = Modifier.height(16.dp))

            //-------------------------------------------
// VIEW WINNERS
//-------------------------------------------
            var viewWinnersFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onViewWinners,
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp, minHeight = 50.dp)
                    .width(buttonWidth)
                    .focusRequester(focusViewWinners)
                    .onFocusChanged { viewWinnersFocused = it.isFocused }
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
                    containerColor = if (viewWinnersFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (viewWinnersFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = stringResource(R.string.btn_view_winners))
            }

            Spacer(modifier = Modifier.height(16.dp))

            //-------------------------------------------
            // EXIT BUTTON
            //-------------------------------------------
            var exitFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onExit,
                modifier = Modifier
                    .defaultMinSize(minWidth = 300.dp, minHeight = 50.dp)
                    .width(buttonWidth)
                    .focusRequester(focusExit)
                    .onFocusChanged { exitFocused = it.isFocused }
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
                    containerColor = if (exitFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (exitFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = stringResource(R.string.btn_exit))
            }

            Spacer(modifier = Modifier.height(24.dp))

            //-------------------------------------------
            // COPYRIGHT
            //-------------------------------------------
            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
