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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.Key.Companion.DirectionLeft
import androidx.compose.ui.input.key.Key.Companion.DirectionRight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import `in`.mahato.tambola.ui.theme.AppTheme
import `in`.mahato.tambola.util.GeneralUtil
import `in`.mahato.tambola.util.ScreenSizeUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

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

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val hindiResult = tts.setLanguage(Locale("hi", "IN"))
                if (hindiResult == TextToSpeech.LANG_MISSING_DATA ||
                    hindiResult == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    tts.setLanguage(Locale.ENGLISH)
                }
            }
        }

        setContent {
            AppTheme {
                TambolaScreen(db, tts, isNewGame)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambolaScreen(db: AppDatabase, tts: TextToSpeech, isNewGame: Boolean) {
    val dao = db.calledNumberDao()
    var calledNumbers by remember { mutableStateOf(listOf<Int>()) }
    var lastNumber by remember { mutableStateOf<Int?>(null) }
    var isAutoCalling by remember { mutableStateOf(false) } // State for Auto Call
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition()
    val flashColor by infiniteTransition.animateColor(
        initialValue = Color.Gray,
        targetValue = Color.Cyan,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Function to call number
    fun callNumber() {
        val remainingNumbers = (1..90).toSet() - calledNumbers.toSet()
        if (remainingNumbers.isNotEmpty()) {
            val newNumber = remainingNumbers.random()
            calledNumbers = calledNumbers + newNumber
            lastNumber = newNumber

            scope.launch {
                dao.resetLast()
                dao.insert(CalledNumber(newNumber, isLast = true))
            }

            val funnyPhrase = FunnyPhraseUtil.getFunnyPhrase(newNumber)
            tts.speak(funnyPhrase, TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            isAutoCalling = false
        }
    }

    // ⭐ Auto Call Logic
    LaunchedEffect(isAutoCalling) {
        if (isAutoCalling) {
            while (isAutoCalling && calledNumbers.size < 90) {
                callNumber()
                delay(4000) // 4 seconds interval
            }
            isAutoCalling = false
        }
    }

    // Load from database on start
    LaunchedEffect(Unit) {
        if (isNewGame) {
            dao.resetBoard()
            calledNumbers = listOf()
            lastNumber = null
        } else {
            val numbers = dao.getAll()
            calledNumbers = numbers.map { it.number }
            lastNumber = numbers.find { it.isLast }?.number
        }
    }

    fun resetBoard() {
        isAutoCalling = false
        calledNumbers = listOf()
        lastNumber = null
        scope.launch { dao.resetBoard() }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tambola Board",
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onPrimary,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // --- DPAD Focus Requesters ---
                    val frCall = remember { FocusRequester() }
                    val frAuto = remember { FocusRequester() }
                    val frReset = remember { FocusRequester() }
                    val frExit = remember { FocusRequester() }
                    val requesters = remember { listOf(frCall, frAuto, frReset, frExit) }

                    var isCallFocused by remember { mutableStateOf(false) }
                    var isAutoFocused by remember { mutableStateOf(false) }
                    var isResetFocused by remember { mutableStateOf(false) }
                    var isExitFocused by remember { mutableStateOf(false) }

                    LaunchedEffect(Unit) {
                        delay(100)
                        frCall.requestFocus()
                    }

                    fun handleDpad(event: KeyEvent, current: Int): Boolean {
                        if (event.type != KeyEventType.KeyDown) return false
                        when (event.key) {
                            DirectionRight -> {
                                requesters[(current + 1) % 4].requestFocus()
                                return true
                            }
                            DirectionLeft -> {
                                requesters[(current - 1 + 4) % 4].requestFocus()
                                return true
                            }
                        }
                        return false
                    }

                    Button(
                        onClick = { callNumber() },
                        modifier = Modifier
                            .focusRequester(frCall)
                            .onFocusChanged { isCallFocused = it.isFocused }
                            .onKeyEvent { handleDpad(it, 0) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isCallFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,
                            contentColor = if (isCallFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary
                        )
                    ) { Text("Call Number") }

                    // ⭐ Added Auto Call / Pause Button
                    Button(
                        onClick = { isAutoCalling = !isAutoCalling },
                        modifier = Modifier
                            .focusRequester(frAuto)
                            .onFocusChanged { isAutoFocused = it.isFocused }
                            .onKeyEvent { handleDpad(it, 1) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isAutoFocused) MaterialTheme.colorScheme.background
                            else if (isAutoCalling) Color(0xFFFFB300) // Amber when active
                            else MaterialTheme.colorScheme.primaryContainer,
                            contentColor = if (isAutoFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary
                        )
                    ) { Text(if (isAutoCalling) "Pause" else "Auto Call") }

                    Button(
                        onClick = { showResetDialog = true },
                        modifier = Modifier
                            .focusRequester(frReset)
                            .onFocusChanged { isResetFocused = it.isFocused }
                            .onKeyEvent { handleDpad(it, 2) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isResetFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,
                            contentColor = if (isResetFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary
                        )
                    ) { Text("Reset Board") }

                    Button(
                        onClick = { showExitDialog = true },
                        modifier = Modifier
                            .focusRequester(frExit)
                            .onFocusChanged { isExitFocused = it.isFocused }
                            .onKeyEvent { handleDpad(it, 3) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isExitFocused) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,
                            contentColor = if (isExitFocused) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimary
                        )
                    ) { Text("Exit") }
                }

                // RESET DIALOG
                if (showResetDialog) {
                    AlertDialog(
                        onDismissRequest = { showResetDialog = false },
                        title = { Text("Reset Game?", color = Color.Red) },
                        text = { Text("Are you sure you want to reset? This clears called numbers.") },
                        confirmButton = {
                            TextButton(onClick = {
                                resetBoard()
                                showResetDialog = false
                            }) { Text("Yes") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showResetDialog = false }) { Text("No") }
                        }
                    )
                }

                // EXIT DIALOG
                if (showExitDialog) {
                    AlertDialog(
                        onDismissRequest = { showExitDialog = false },
                        title = { Text("Exit Game?", color = Color.Red) },
                        text = { Text("Are you sure you want to exit?") },
                        confirmButton = {
                            TextButton(onClick = {
                                (context as GameActivity).finish()
                                showExitDialog = false
                            }) { Text("Yes") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showExitDialog = false }) { Text("No") }
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