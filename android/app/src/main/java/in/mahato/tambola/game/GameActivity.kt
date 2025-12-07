package `in`.mahato.tambola.game

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.*
import androidx.room.Room
import `in`.mahato.tambola.MainScreenComposable
import `in`.mahato.tambola.ui.theme.AppTheme
import `in`.mahato.tambola.ui.theme.PurpleBg
import `in`.mahato.tambola.util.GeneralUtil
import `in`.mahato.tambola.util.ScreenSizeUtil
import kotlinx.coroutines.launch
import java.util.*





/** --------------------- ACTIVITY --------------------- **/

class GameActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isNewGame = intent.getBooleanExtra("NEW_GAME", false)
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tambola_db"
        ).build()

        tts = TextToSpeech(this){ status ->
            if (status == TextToSpeech.SUCCESS) {

                // Try Hindi first
                val hindiResult = tts.setLanguage(Locale("hi", "IN"))

                if (hindiResult == TextToSpeech.LANG_MISSING_DATA ||
                    hindiResult == TextToSpeech.LANG_NOT_SUPPORTED) {

                    // Fallback to English
                    tts.setLanguage(Locale.ENGLISH)
                }
                tts.speak("Welcome to the Tambola Board!", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }



        setContent {
            AppTheme {
                TambolaScreen(db, tts,isNewGame)
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}

/** --------------------- COMPOSABLE --------------------- **/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambolaScreen(db: AppDatabase, tts: TextToSpeech, isNewGame: Boolean) {
    val dao = db.calledNumberDao()
    var calledNumbers by remember { mutableStateOf(listOf<Int>()) }
    var lastNumber by remember { mutableStateOf<Int?>(null) }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    // Animation for last called number
    val infiniteTransition = rememberInfiniteTransition()
    val flashColor by infiniteTransition.animateColor(
        initialValue = Color.Gray,
        targetValue = Color.Cyan,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Load from database on start
    LaunchedEffect(Unit) {
        if (isNewGame) {
            dao.resetBoard()        // clear DB
            calledNumbers = listOf()
            lastNumber = null
        } else {
            val numbers = dao.getAll()
            calledNumbers = numbers.map { it.number }
            lastNumber = numbers.find { it.isLast }?.number
        }
    }

    // Function to call number
    fun callNumber() {
        val remainingNumbers = (1..90).toSet() - calledNumbers.toSet()
        if (remainingNumbers.isNotEmpty()) {
            val newNumber = remainingNumbers.random()
            calledNumbers = calledNumbers + newNumber
            lastNumber = newNumber

            // Update DB in coroutine
            scope.launch {
                dao.resetLast()
                dao.insert(CalledNumber(newNumber, isLast = true))
            }

            // TTS announcement
            val funnyPhrase = FunnyPhraseUtil.getFunnyPhrase(newNumber)
            tts.speak(funnyPhrase, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // Function to reset board
    fun resetBoard() {
        calledNumbers = listOf()
        lastNumber = null
        scope.launch { dao.resetBoard() }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = { CenterAlignedTopAppBar(
            title = {
                Text("Tambola Board",
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onPrimary,
            overflow = TextOverflow.Ellipsis) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary // Set your desired background color here
            ))
                 },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var showResetDialog by remember { mutableStateOf(false) }
                var showExitDialog by remember { mutableStateOf(false) }
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                    var isCallNumberFocused by remember { mutableStateOf(false) }
                    var isResetFocused by remember { mutableStateOf(false) }
                    var isExitFocused by remember { mutableStateOf(false) }
                    Button(onClick = { callNumber() },
                        modifier = Modifier.onFocusChanged { focusState ->
                            isCallNumberFocused = focusState.isFocused
                            },
                        colors = ButtonDefaults.buttonColors(
                        containerColor = if(isCallNumberFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,      // Background
                        contentColor = if(isCallNumberFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary     // Text color
                    )) { Text("Call Number") }
                    Button(onClick = { showResetDialog = true },
                        modifier = Modifier.onFocusChanged { focusState ->
                            isResetFocused = focusState.isFocused
                        },colors = ButtonDefaults.buttonColors(
                            containerColor = if(isResetFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,      // Background
                            contentColor = if(isResetFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary     // Text color
                    )) { Text("Reset Board") }
                    Button(onClick = { showExitDialog = true },
                        modifier = Modifier.onFocusChanged { focusState ->
                            isExitFocused = focusState.isFocused
                        },colors = ButtonDefaults.buttonColors(
                            containerColor = if(isExitFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,      // Background
                            contentColor = if(isExitFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary     // Text color
                    )) { Text("Exit") }
                }
                // RESET CONFIRMATION DIALOG
                if (showResetDialog) {
                    AlertDialog(
                        onDismissRequest = { showResetDialog = false },

                        title = { Text("Reset Game?", color = Color.Red  ) },

                        text = {
                            Text("Are you sure you want to reset the current game? This will clear called numbers for this session.")
                        },

                        confirmButton = {
                            TextButton(onClick = {
                                resetBoard()
                                showResetDialog = false
                            }) {
                                Text("Yes")
                            }
                        },

                        dismissButton = {
                            TextButton(onClick = { showResetDialog = false }) {
                                Text("No")
                            }
                        }
                    )
                }
                // EXIT CONFIRMATION DIALOG
                if (showExitDialog) {
                    AlertDialog(
                        onDismissRequest = { showExitDialog = false },

                        title = { Text("Exit Game?", color = Color.Red  ) },

                        text = {
                            Text("Are you sure you want to exit the Tambola Board?")
                        },

                        confirmButton = {
                            TextButton(onClick = {
                                (context as GameActivity).finish()
                                showExitDialog = false
                            }) {
                                Text("Yes")
                            }
                        },

                        dismissButton = {
                            TextButton(onClick = { showExitDialog = false }) {
                                Text("No")
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Last Called Number: ${lastNumber ?: "-"}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(10),
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        items((1..90).toList()) { number ->
                            val isCalled = calledNumbers.contains(number)
                            val isLast = lastNumber == number
                            val bgColor = when {
                                isLast -> flashColor
                                isCalled -> Color.Magenta

                                else -> MaterialTheme.colorScheme.secondary
                            }
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size((ScreenSizeUtil.getScreenHeightDp(context) * 0.55 * 0.1).dp)
                                   // .size(40.dp)
                                    .background(
                                        color = bgColor,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "$number")
                            }
                        }

                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = GeneralUtil.getCopyrightMessage(),
                    style = MaterialTheme.typography.bodyMedium
                )

            }
        }
    )
}
