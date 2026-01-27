package `in`.mahato.tambola.gamemode

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import `in`.mahato.tambola.game.GameActivity
import `in`.mahato.tambola.util.GeneralUtil
import androidx.compose.ui.res.stringResource
import `in`.mahato.tambola.R

@Composable
fun GameIdInputScreen(
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var gameId by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val submitFocusRequester = remember { FocusRequester() }
    var submitFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.title_enter_game_id),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = gameId,
                onValueChange = {
                    gameId = it.uppercase()
                    errorMessage = null 
                },
                label = { Text(stringResource(R.string.label_game_id)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters,
                    autoCorrect = false
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionDown) {
                            submitFocusRequester.requestFocus()
                            true
                        } else false
                    },
                isError = errorMessage != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White
                )
            )

            Text(
                text = stringResource(R.string.hint_game_id),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (gameId.isBlank()) {
                        errorMessage = context.getString(R.string.error_empty_game_id)
                        return@Button
                    }
                    isLoading = true
                    errorMessage = null
                    
                    isLoading = true
                    errorMessage = null

                    scope.launch {
                        try {
                            withTimeout(30000) { // 30 seconds timeout
                                val auth = FirebaseAuth.getInstance()
                                if (auth.currentUser == null) {
                                    errorMessage = context.getString(R.string.status_authenticating) // Show status
                                    auth.signInAnonymously().await()
                                    android.util.Log.d("GameIdInputScreen", "signInAnonymously:success")
                                }
                                
                                errorMessage = context.getString(R.string.status_verifying_game_id)
                                val db = FirebaseFirestore.getInstance()
                                val docRef = db.collection("games").document(gameId.trim())
                                val snapshot = docRef.get().await()

                                isLoading = false
                                if (snapshot.exists()) {
                                    // Game ID found
                                    val intent = Intent(context, `in`.mahato.tambola.rule.RuleSelectionActivity::class.java)
                                    intent.putExtra("NEW_GAME", true)
                                    intent.putExtra("GAME_ID", gameId.trim())
                                    intent.putExtra("IS_MODERATED", true)
                                    context.startActivity(intent)
                                    onFinish()
                                } else {
                                    // Game ID not found
                                    errorMessage = context.getString(R.string.error_game_id_not_found)
                                }
                            }
                        } catch (e: Exception) {
                            isLoading = false
                            e.printStackTrace()
                            if (e is kotlinx.coroutines.TimeoutCancellationException) {
                                errorMessage = context.getString(R.string.error_verification_timeout)
                            } else {
                                // Show full error for debugging
                                errorMessage = context.getString(R.string.error_generic, e.message, e.cause?.message)
                                
                                if (e.message?.contains("Default FirebaseApp is not initialized") == true) {
                                     errorMessage = context.getString(R.string.error_firebase_config_missing)
                                } else if (e.message?.contains("FirebaseApp") == true) {
                                     errorMessage = context.getString(R.string.error_firebase_config_missing_short)
                                } else if (e.message?.contains("restricted to administrators") == true || e.message?.contains("OPERATION_NOT_ALLOWED") == true) {
                                     errorMessage = context.getString(R.string.error_auth_disabled)
                                }
                            }
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .focusRequester(submitFocusRequester)
                    .onFocusChanged { submitFocused = it.isFocused }
                    .onKeyEvent {
                        if (it.type == KeyEventType.KeyDown && it.key == Key.DirectionUp) {
                            focusRequester.requestFocus()
                            true
                        } else false
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (submitFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (submitFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    disabledContentColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    Text(text = stringResource(R.string.btn_submit), fontSize = 18.sp)
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
