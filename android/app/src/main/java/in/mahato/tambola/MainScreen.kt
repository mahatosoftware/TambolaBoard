package `in`.mahato.tambola

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
            var newGameButtonFocused by remember { mutableStateOf(false) }

            Button(
                onClick = onNewGame,
                modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 50.dp).width(buttonWidth).onFocusChanged { focusState ->
                    newGameButtonFocused = focusState.isFocused
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(newGameButtonFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,      // Background
                    contentColor = if(newGameButtonFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary     // Text color
                )

            ) {
                Text(text = "Start New Game")
            }

            Spacer(modifier = Modifier.height(16.dp))
            var continueGameFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onContinue,
                modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 50.dp).width(buttonWidth).onFocusChanged { focusState ->
                    continueGameFocused = focusState.isFocused
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(continueGameFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,      // Background
                    contentColor = if(continueGameFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary     // Text color
                )
            ) {
                Text(text = "Continue Last Game")
            }
            Spacer(modifier = Modifier.height(16.dp))
            var exitGameFocused by remember { mutableStateOf(false) }
            Button(
                onClick = onExit,
                modifier = Modifier.defaultMinSize(minWidth = 300.dp, minHeight = 50.dp).width(buttonWidth).onFocusChanged { focusState ->
                    exitGameFocused = focusState.isFocused
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(exitGameFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,      // Background
                    contentColor = if(exitGameFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary     // Text color
                )
            ) {
                Text(text = "Exit")
            }

            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
