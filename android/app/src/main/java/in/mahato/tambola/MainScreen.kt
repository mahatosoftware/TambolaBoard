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
import androidx.compose.ui.unit.*
import `in`.mahato.tambola.util.GeneralUtil
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNewGame: () -> Unit,
    onContinue: () -> Unit,
    onExit: () -> Unit
) {
    val year = Calendar.getInstance().get(Calendar.YEAR)

    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val buttonWidth = screenWidth * 0.3f

    // -----------------------------
    // Focus Requesters for 3 buttons
    // -----------------------------
    val focusNew = remember { FocusRequester() }
    val focusContinue = remember { FocusRequester() }
    val focusExit = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusNew.requestFocus()
    }

    fun moveFocusRight(current: Int) {
        when (current) {
            0 -> focusContinue.requestFocus()
            1 -> focusExit.requestFocus()
            2 -> focusNew.requestFocus()
        }
    }

    fun moveFocusLeft(current: Int) {
        when (current) {
            0 -> focusExit.requestFocus()
            1 -> focusNew.requestFocus()
            2 -> focusContinue.requestFocus()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "Tambola time. The Party Starts here!!")
            Spacer(modifier = Modifier.height(16.dp))

            //-------------------------------------------
            // NEW GAME BUTTON
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
                                Key.DirectionDown -> { moveFocusRight(0); true }
                                Key.DirectionUp -> { moveFocusLeft(0); true }
                                else -> false
                            }
                        } else false
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (newGameFocused) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.primaryContainer,
                    contentColor =
                        if (newGameFocused) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Start New Game")
            }

            Spacer(modifier = Modifier.height(16.dp))

            //-------------------------------------------
            // CONTINUE BUTTON
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
                                Key.DirectionDown -> { moveFocusRight(1); true }
                                Key.DirectionUp -> { moveFocusLeft(1); true }
                                else -> false
                            }
                        } else false
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (continueFocused) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.primaryContainer,
                    contentColor =
                        if (continueFocused) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Continue Last Game")
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
                                Key.DirectionDown -> { moveFocusRight(2); true }
                                Key.DirectionUp -> { moveFocusLeft(2); true }
                                else -> false
                            }
                        } else false
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor =
                        if (exitFocused) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.primaryContainer,
                    contentColor =
                        if (exitFocused) MaterialTheme.colorScheme.onBackground
                        else MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Exit")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
