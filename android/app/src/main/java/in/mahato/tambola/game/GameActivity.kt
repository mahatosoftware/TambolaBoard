package `in`.mahato.tambola.game

import android.content.res.Configuration
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.key.Key.Companion.DirectionLeft
import androidx.compose.ui.input.key.Key.Companion.DirectionRight
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    private var _isTtsReady = mutableStateOf(false)

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
                _isTtsReady.value = true
                tts.speak("Welcome to Tambola Board", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        setContent {
            AppTheme {
                TambolaScreen(db, tts, isNewGame, _isTtsReady.value)
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
fun TambolaScreen(db: AppDatabase, tts: TextToSpeech, isNewGame: Boolean, isTtsReady: Boolean) {
    val dao = db.calledNumberDao()
    var calledNumbers by remember { mutableStateOf(listOf<Int>()) }
    var lastNumber by remember { mutableStateOf<Int?>(null) }
    var isAutoCalling by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val infiniteTransition = rememberInfiniteTransition()
    val flashColor by infiniteTransition.animateColor(
        initialValue = Color.Gray,
        targetValue = Color.Cyan,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )

    fun callNumber() {
        if (!isTtsReady) return
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

    LaunchedEffect(isAutoCalling) {
        if (isAutoCalling) {
            while (isAutoCalling && calledNumbers.size < 90) {
                callNumber()
                delay(6000)
            }
            isAutoCalling = false
        }
    }

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
                title = { Text("Tambola Board", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        val contentModifier = Modifier.fillMaxSize().padding(padding).padding(8.dp)

        if (isLandscape) {
            Row(modifier = contentModifier, verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier.weight(0.4f).fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    GameControls(isAutoCalling, { callNumber() }, { isAutoCalling = !isAutoCalling },
                        { resetBoard() }, { (context as GameActivity).finish() }, lastNumber, true, isTtsReady)
                }
                Column(modifier = Modifier.weight(0.6f)) {
                    NumberGrid(calledNumbers, lastNumber, flashColor)
                }
            }
        } else {
            Column(
                modifier = contentModifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                GameControls(isAutoCalling, { callNumber() }, { isAutoCalling = !isAutoCalling },
                    { resetBoard() }, { (context as GameActivity).finish() }, lastNumber, false, isTtsReady)

                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    NumberGrid(calledNumbers, lastNumber, flashColor)
                }

                Text(
                    text = GeneralUtil.getCopyrightMessage(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameControls(
    isAutoCalling: Boolean,
    onCall: () -> Unit,
    onAutoToggle: () -> Unit,
    onResetClick: () -> Unit,
    onExitClick: () -> Unit,
    lastNumber: Int?,
    isLandscape: Boolean,
    isTtsReady: Boolean
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    val frCall = remember { FocusRequester() }
    val frAuto = remember { FocusRequester() }
    val frReset = remember { FocusRequester() }
    val frExit = remember { FocusRequester() }
    val requesters = remember { listOf(frCall, frAuto, frReset, frExit) }
    var focusedIndex by remember { mutableIntStateOf(0) }

    fun handleDpad(event: KeyEvent, current: Int): Boolean {
        if (event.type != KeyEventType.KeyDown) return false
        when (event.key) {
            DirectionRight -> { requesters[(current + 1) % 4].requestFocus(); return true }
            DirectionLeft -> { requesters[(current - 1 + 4) % 4].requestFocus(); return true }
        }
        return false
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.padding(vertical = if (isLandscape) 8.dp else 16.dp).fillMaxWidth(if (isLandscape) 0.8f else 0.7f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(if (isLandscape) 8.dp else 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LAST NUMBER", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = "${lastNumber ?: "--"}",
                    style = if (isLandscape) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        Box(modifier = Modifier.height(120.dp), contentAlignment = Alignment.Center) {
            AnimatedContent(
                targetState = isTtsReady,
                transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) },
                label = "TtsLoading"
            ) { ready ->
                if (ready) {
                    FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, maxItemsInEachRow = 2) {
                        val getBtnColors = @Composable { index: Int ->
                            ButtonDefaults.buttonColors(
                                containerColor = if (focusedIndex == index) MaterialTheme.colorScheme.background
                                else if (index == 1 && isAutoCalling) Color(0xFFFFB300) else MaterialTheme.colorScheme.primaryContainer,
                                contentColor = if (focusedIndex == index) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Button(onClick = onCall, colors = getBtnColors(0), modifier = Modifier.padding(4.dp).focusRequester(frCall).onFocusChanged { if (it.isFocused) focusedIndex = 0 }.onKeyEvent { handleDpad(it, 0) }) { Text("Call Next") }
                        Button(onClick = onAutoToggle, colors = getBtnColors(1), modifier = Modifier.padding(4.dp).focusRequester(frAuto).onFocusChanged { if (it.isFocused) focusedIndex = 1 }.onKeyEvent { handleDpad(it, 1) }) { Text(if (isAutoCalling) "Pause" else "Auto Call") }
                        Button(onClick = { showResetDialog = true }, colors = getBtnColors(2), modifier = Modifier.padding(4.dp).focusRequester(frReset).onFocusChanged { if (it.isFocused) focusedIndex = 2 }.onKeyEvent { handleDpad(it, 2) }) { Text("Reset") }
                        Button(onClick = { showExitDialog = true }, colors = getBtnColors(3), modifier = Modifier.padding(4.dp).focusRequester(frExit).onFocusChanged { if (it.isFocused) focusedIndex = 3 }.onKeyEvent { handleDpad(it, 3) }) { Text("Exit") }
                    }
                } else {
                    val infiniteLoading = rememberInfiniteTransition(label = "loading")
                    val scale by infiniteLoading.animateFloat(
                        initialValue = 0.8f, targetValue = 1.2f,
                        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse), label = "scale"
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp).scale(scale),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 4.dp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Board is getting Ready for use ...", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Game?", color = Color.Red) },
            text = { Text("Are you sure you want to reset the board?") },
            confirmButton = { TextButton(onClick = { onResetClick(); showResetDialog = false }) { Text("Yes") } },
            dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text("No") } }
        )
    }

    if (showExitDialog) {
        AlertDialog(onDismissRequest = { showExitDialog = false },
            title = { Text("Exit Game?", color = Color.Red) },
            text = { Text("Are you sure you want to exit the current game?") },
            confirmButton = { TextButton(onClick = { onExitClick(); showExitDialog = false }) { Text("Yes") } },
            dismissButton = { TextButton(onClick = { showExitDialog = false }) { Text("No") } }
        )
    }
}

@Composable
fun NumberGrid(calledNumbers: List<Int>, lastNumber: Int?, flashColor: Color) {
    val configuration = LocalConfiguration.current
    val cellSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        (configuration.screenHeightDp * 0.08).dp
    } else {
        (configuration.screenWidthDp * 0.085).dp
    }

    LazyVerticalGrid(columns = GridCells.Fixed(10), modifier = Modifier.fillMaxWidth().wrapContentHeight(), contentPadding = PaddingValues(4.dp), userScrollEnabled = false) {
        items((1..90).toList()) { number ->
            val isCalled = calledNumbers.contains(number)
            val isLast = lastNumber == number
            val bgColor = when {
                isLast -> flashColor
                isCalled -> Color.Magenta
                else -> MaterialTheme.colorScheme.secondary
            }
            Box(
                modifier = Modifier.padding(2.dp).size(cellSize).background(color = bgColor, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$number", fontSize = (cellSize.value * 0.4).sp, fontWeight = FontWeight.Bold,
                    color = if (isCalled || isLast) Color.White else MaterialTheme.colorScheme.onSecondary)
            }
        }
    }
}