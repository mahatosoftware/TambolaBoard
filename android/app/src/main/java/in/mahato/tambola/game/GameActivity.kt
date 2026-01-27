package `in`.mahato.tambola.game

import android.app.Activity
import android.content.Intent
import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.Key.Companion.DirectionLeft
import androidx.compose.ui.input.key.Key.Companion.DirectionRight
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController // Added
import androidx.compose.ui.input.key.Key // Added
import androidx.compose.ui.input.key.nativeKeyCode // Added checks if needed or just Key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.room.Room
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import `in`.mahato.tambola.MainActivity
import `in`.mahato.tambola.db.AppDatabase
import `in`.mahato.tambola.game.entity.CalledNumber
import `in`.mahato.tambola.game.entity.GameMetadata
import `in`.mahato.tambola.game.util.FunnyPhraseUtil
import `in`.mahato.tambola.rule.entity.WinningPrizeEntity
import `in`.mahato.tambola.ui.theme.AppTheme
import `in`.mahato.tambola.ui.theme.PurpleDark
import `in`.mahato.tambola.util.GeneralUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import `in`.mahato.tambola.game.entity.PlayerEntity
import `in`.mahato.tambola.rule.entity.SavedRuleEntity
import kotlinx.coroutines.withContext
import java.util.Locale
import androidx.compose.ui.res.stringResource
import `in`.mahato.tambola.R

class GameActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech
    private var _isTtsReady = mutableStateOf(false)
    private var isInPipMode = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isNewGame = intent.getBooleanExtra("NEW_GAME", false)
        val intentGameId = intent.getStringExtra("GAME_ID") ?: ""
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tambola_db"
        ).fallbackToDestructiveMigration().build()

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.forLanguageTag("hi-IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    tts.setLanguage(Locale.forLanguageTag("en-US"))
                }
                _isTtsReady.value = true
                tts.speak(getString(R.string.welcome_speech), TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
        enableEdgeToEdge()
        setContent {
            var showWinnerBoard by remember { mutableStateOf(false) }
            AppTheme {
                TambolaScreen(db, tts, isNewGame, intentGameId, _isTtsReady.value, isInPipMode.value)
            }

            if (showWinnerBoard) {
                WinnerBoardDialog(
                    db = AppDatabase.getDatabase(this),
                    gameId = intentGameId, // Pass gameId (from intent or empty, logic inside handles it)
                    onDismiss = { showWinnerBoard = false },
                    onReloadPlayers = {}
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPictureInPictureMode(PictureInPictureParams.Builder().build())
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isInPipMode.value = isInPictureInPictureMode
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambolaScreen(db: AppDatabase, tts: TextToSpeech, isNewGame: Boolean, intentGameId: String, isTtsReady: Boolean, isInPipMode: Boolean) {
    val dao = db.calledNumberDao()
    var calledNumbers by remember { mutableStateOf(listOf<Int>()) }
    var lastNumber by remember { mutableStateOf<Int?>(null) }
    var isAutoCalling by remember { mutableStateOf(false) }
    var gameId by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val infiniteTransition = rememberInfiniteTransition(label = "flash")
    val flashColor by infiniteTransition.animateColor(
        initialValue = Color.Gray,
        targetValue = Color.Cyan,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ), label = "flash"
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

            val funnyphrase = FunnyPhraseUtil.getFunnyPhrase(newNumber)
            tts.speak("$funnyphrase", TextToSpeech.QUEUE_FLUSH, null, null)
        } else {
            isAutoCalling = false
        }
    }

    fun undoLastNumber() {
        if (calledNumbers.isNotEmpty()) {
            // Remove last number from list
            val numberToRemove = calledNumbers.last()
            calledNumbers = calledNumbers.dropLast(1)
            
            // Determine new last number
            val newLastNumber = if (calledNumbers.isNotEmpty()) calledNumbers.last() else null
            lastNumber = newLastNumber

            scope.launch {
                dao.delete(numberToRemove)
                if (newLastNumber != null) {
                    dao.setLast(newLastNumber)
                }
            }
        }
    }

    suspend fun syncPlayers(gId: String) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val playersSnapshot = firestore.collection("games")
                .document(gId)
                .collection("tickets")
                .get()
                .await()

            val players = playersSnapshot.documents.mapNotNull { doc ->
                val name = doc.getString("name")
                if (!name.isNullOrEmpty()) {
                    PlayerEntity(
                        gameId = gId,
                        name = name
                    )
                } else null
            }

            // Save to local DB
            withContext(Dispatchers.IO) {
                db.playerDao().deleteByGameId(gId) // Clear old if any
                db.playerDao().insertAll(players)
            }
            android.util.Log.d("GameActivity", "Synced ${players.size} players for game $gId")
        } catch (e: Exception) {
            android.util.Log.e("GameActivity", "Error syncing players: ${e.message}")
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
        val savedId = dao.getSavedGameId()
        if (isNewGame || savedId == null) {
            dao.resetBoard()
            // Use intent ID if available, otherwise generate fallback (should ideally always be passed)
            val newId = if (intentGameId.isNotEmpty()) intentGameId else GeneralUtil.generateGameId()
            dao.saveGameMetadata(GameMetadata(gameId = newId))

            // Clear previous game prizes and insert Saved Rules
            withContext(Dispatchers.IO) {
                db.winningPrizeDao().clearAll()
                
                val savedRules = db.ruleDao().getAllSavedRules()
                if (savedRules.isNotEmpty()) {
                    savedRules.forEach { rule ->
                        // Fix: Insert 'quantity' number of prizes for each rule
                        repeat(rule.quantity) {
                             db.winningPrizeDao().insert(
                                 WinningPrizeEntity(savedRule = rule, winnerName = null)
                             )
                        }
                    }
                }
            }

            // If this is a Moderated Game (has intent ID), sync players from Firestore
            if (intentGameId.isNotEmpty()) {
                launch {
                    syncPlayers(newId)
                }
            }

            gameId = newId
            calledNumbers = listOf()
            lastNumber = null
        } else {
            gameId = savedId
            val numbers = dao.getAll()
            calledNumbers = numbers.map { it.number }
            lastNumber = numbers.find { it.isLast }?.number
        }
    }

    fun resetBoard() {
        isAutoCalling = false
        // reuse current ID or generate new if one wasn't passed initially?
        // User requested removing generation from Activity, so we should probably reuse the current one
        // or re-generate using Util if strictly "New Game" behavior is desired.
        // Assuming Reset means "Restart THIS game/session" or "Start fresh game".
        // Let's generate a NEW ID using Util to maintain "Reset" behavior but implementation via Util.
        val newId = GeneralUtil.generateGameId() 
        calledNumbers = listOf()
        lastNumber = null
        gameId = newId
        scope.launch {
            dao.resetBoard()
            dao.saveGameMetadata(GameMetadata(gameId = newId))
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            if (!isInPipMode) {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.app_name), color = MaterialTheme.colorScheme.onPrimary) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /** MAIN CONTENT **/
            if (isInPipMode) {
                // In PiP mode, show only the number grid, full width/height
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    NumberGrid(calledNumbers, lastNumber, flashColor)
                }
            } else if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(0.4f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        GameControls(
                            db = db,
                            isAutoCalling,
                            { callNumber() },
                            { isAutoCalling = !isAutoCalling },
                            { resetBoard() },
                            {
                                val activity = context as Activity
                                val intent = Intent(activity, MainActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                }
                                activity.startActivity(intent)
                                activity.finish()
                                },
                            lastNumber,
                            true,
                            isTtsReady,
                            gameId,
                            onReloadPlayers = { scope.launch { syncPlayers(gameId) } },
                            onUndo = { undoLastNumber() },
                            calledNumbers = calledNumbers
                        )
                    }
                    Column(modifier = Modifier.weight(0.6f)) {
                        NumberGrid(calledNumbers, lastNumber, flashColor)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameControls(
                        db = db,
                        isAutoCalling,
                        { callNumber() },
                        { isAutoCalling = !isAutoCalling },
                        { resetBoard() },
                        {
                            val activity = context as Activity
                            val intent = Intent(activity, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                            }
                            activity.startActivity(intent)
                            activity.finish()
                        },
                        lastNumber,
                        false,
                        isTtsReady,
                        gameId,
                        onReloadPlayers = { scope.launch { syncPlayers(gameId) } },
                        onUndo = { undoLastNumber() },
                        calledNumbers = calledNumbers
                    )

                 //   Spacer(Modifier.weight(1f))

                    NumberGrid(calledNumbers, lastNumber, flashColor)
                }
            }

            /** ✅ FOOTER – WORKS ON TV **/

            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            )
        }
    }


}// end of TambolaScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameControls(
    db: AppDatabase,
    isAutoCalling: Boolean,
    onCall: () -> Unit,
    onAutoToggle: () -> Unit,
    onResetClick: () -> Unit,
    onBackClick: () -> Unit,
    lastNumber: Int?,
    isLandscape: Boolean,
    isTtsReady: Boolean,
    gameId: String,
    onReloadPlayers: () -> Unit,
    onUndo: () -> Unit,
    calledNumbers: List<Int>
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showWinnerBoard by remember { mutableStateOf(false) }
    var showUndoDialog by remember { mutableStateOf(false) }

    val frCall = remember { FocusRequester() }
    val frAuto = remember { FocusRequester() }
    val frWinner = remember { FocusRequester() }
    val frReset = remember { FocusRequester() }
    val frExit = remember { FocusRequester() }
    val frUndo = remember { FocusRequester() }

    val requesters = remember { listOf(frCall, frAuto, frWinner, frUndo, frReset, frExit) }
    var focusedIndex by remember { mutableIntStateOf(0) }

    fun handleDpad(event: KeyEvent, current: Int): Boolean {
        if (event.type != KeyEventType.KeyDown) return false
        when (event.key) {
            DirectionRight -> { requesters[(current + 1) % 6].requestFocus(); return true }
            DirectionLeft -> { requesters[(current - 1 + 6) % 6].requestFocus(); return true }
        }
        return false
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        if (gameId.isNotEmpty()) {
            val qrBitmap = remember(gameId) { generateQRCode(gameId) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                qrBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = stringResource(R.string.game_qr_desc),
                        modifier = Modifier
                            .size(if (isLandscape) 60.dp else 90.dp)
                            .background(Color.White)
                            .padding(4.dp)
                            .border(1.dp, Color.Black)
                    )
                }
                Text(
                    stringResource(R.string.game_id_label) + gameId,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        Card(
            modifier = Modifier.padding(vertical = if (isLandscape) 4.dp else 4.dp).fillMaxWidth(if (isLandscape) 0.8f else 0.6f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.last_number_label), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Text(
                    text = "${lastNumber ?: "--"}",
                    style = if (isLandscape) MaterialTheme.typography.displaySmall else MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        // Last 5 Numbers
        if (calledNumbers.size > 1) {
            val history = calledNumbers.dropLast(1).takeLast(5)
            Card(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxWidth(if (isLandscape) 0.9f else 0.8f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Last 5",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        history.forEach { num ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(MaterialTheme.colorScheme.onSecondary, shape = androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$num",
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier.height(130.dp), contentAlignment = Alignment.Center) {
            AnimatedContent(
                targetState = isTtsReady,
                transitionSpec = { fadeIn(tween(500)) togetherWith fadeOut(tween(500)) },
                label = "TtsLoading"
            ) { ready ->
                if (ready) {
                    FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, maxItemsInEachRow = 3) {
                        val getBtnColors = @Composable { index: Int ->
                            ButtonDefaults.buttonColors(
                                containerColor = if (focusedIndex == index) MaterialTheme.colorScheme.background
                                else if (index == 1 && isAutoCalling) Color(0xFFFFB300) else MaterialTheme.colorScheme.primaryContainer,
                                contentColor = if (focusedIndex == index) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.tertiary
                            )
                        }

                        Button(onClick = onCall, colors = getBtnColors(0), modifier = Modifier.padding(4.dp).focusRequester(frCall).onFocusChanged { if (it.isFocused) focusedIndex = 0 }.onKeyEvent { handleDpad(it, 0) }) { Text(stringResource(R.string.btn_call_next)) }
                        Button(onClick = onAutoToggle, colors = getBtnColors(1), modifier = Modifier.padding(4.dp).focusRequester(frAuto).onFocusChanged { if (it.isFocused) focusedIndex = 1 }.onKeyEvent { handleDpad(it, 1) }) { Text(if (isAutoCalling) stringResource(R.string.btn_pause) else stringResource(R.string.btn_auto_call)) }
                        Button(onClick = { showWinnerBoard = true }, colors = getBtnColors(2), modifier = Modifier.padding(4.dp).focusRequester(frWinner).onFocusChanged { if (it.isFocused) focusedIndex = 2 }.onKeyEvent { handleDpad(it, 2) }) { Text(stringResource(R.string.btn_claim_prize)) }
                        Button(onClick = { showUndoDialog = true }, colors = getBtnColors(3), modifier = Modifier.padding(4.dp).focusRequester(frUndo).onFocusChanged { if (it.isFocused) focusedIndex = 3 }.onKeyEvent { handleDpad(it, 3) }) { Text("Undo") }
                        Button(onClick = { showResetDialog = true }, colors = getBtnColors(4), modifier = Modifier.padding(4.dp).focusRequester(frReset).onFocusChanged { if (it.isFocused) focusedIndex = 4 }.onKeyEvent { handleDpad(it, 4) }) { Text(stringResource(R.string.btn_reset)) }
                        Button(onClick = { showExitDialog = true }, colors = getBtnColors(5), modifier = Modifier.padding(4.dp).focusRequester(frExit).onFocusChanged { if (it.isFocused) focusedIndex = 5 }.onKeyEvent { handleDpad(it, 5) }) { Text(stringResource(R.string.btn_return_main_menu)) }
                    }
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }

    if (showWinnerBoard) {
        WinnerBoardDialog(
            db = db, 
            gameId = gameId, 
            onDismiss = { showWinnerBoard = false },
            onReloadPlayers = onReloadPlayers
        )
    }

    if (showUndoDialog) {
        AlertDialog(onDismissRequest = { showUndoDialog = false },
            title = { Text("Undo Last Number?", color = Color.Red) },
            text = { Text("Are you sure you want to undo the last change?") },
            confirmButton = { TextButton(onClick = { onUndo(); showUndoDialog = false }) { Text(stringResource(R.string.dialog_yes)) } },
            dismissButton = { TextButton(onClick = { showUndoDialog = false }) { Text(stringResource(R.string.dialog_no)) } }
        )
    }

    if (showResetDialog) {
        AlertDialog(onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.dialog_reset_title), color = Color.Red) },
            text = { Text(stringResource(R.string.dialog_reset_message)) },
            confirmButton = { TextButton(onClick = { onResetClick(); showResetDialog = false }) { Text(stringResource(R.string.dialog_yes)) } },
            dismissButton = { TextButton(onClick = { showResetDialog = false }) { Text(stringResource(R.string.dialog_no)) } }
        )
    }

    if (showExitDialog) {
        AlertDialog(onDismissRequest = { showExitDialog = false },
            title = { Text(stringResource(R.string.dialog_exit_title), color = Color.Red) },
            text = { Text(stringResource(R.string.dialog_exit_message)) },
            confirmButton = { TextButton(onClick = { onBackClick(); showExitDialog = false }) { Text(stringResource(R.string.dialog_yes)) } },
            dismissButton = { TextButton(onClick = { showExitDialog = false }) { Text(stringResource(R.string.dialog_no)) } }
        )
    }
}

// Utility to generate a scannable QR Code bitmap
fun generateQRCode(text: String): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}
@Composable
fun WinnerBoardDialog(
    db: AppDatabase,
    gameId: String,
    onDismiss: () -> Unit,
    onReloadPlayers: () -> Unit
) {
    val prizes by db.winningPrizeDao()
        .getAllPrizes()
        .collectAsState(initial = emptyList())

        
    val players by db.playerDao()
        .getPlayers(gameId)
        .collectAsState(initial = emptyList())

    val firstItemFocusRequester = remember { FocusRequester() }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.primary,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 500.dp)
                    .heightIn(max = 500.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.dialog_winner_board_title),
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    if (gameId.length == 6) {
                        androidx.compose.material3.IconButton(
                            onClick = onReloadPlayers,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = androidx.compose.material.icons.Icons.Filled.Refresh,
                                contentDescription = stringResource(R.string.desc_reload_players),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
                Text(
                    stringResource(R.string.winner_board_instruction),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(Modifier.height(12.dp))


                // ⭐ FIXED HEADER ROW
                WinnerHeader()

                Box(modifier = Modifier.weight(1f)) {
                    LazyColumn {
                        itemsIndexed(prizes, key = { _, it -> it.prizeId }) { index, prize ->
                            WinnerItemRow(
                                db= db,
                                gameId = gameId,
                                prize = prize,
                                suggestions = players,
                                focusRequester =
                                    if (index == 0) firstItemFocusRequester else null,
                                onClaimToggle = { claimed ->
                                    if (claimed)
                                        db.winningPrizeDao().claimPrize(prize.prizeId)
                                    else
                                        db.winningPrizeDao().unclaimPrize(prize.prizeId)
                                },
                                onReloadPlayers = onReloadPlayers
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
                var closeDialogFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth() .onFocusChanged { closeDialogFocused = it.isFocused },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (closeDialogFocused)
                            MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.primaryContainer,
                        contentColor = if (closeDialogFocused)
                            MaterialTheme.colorScheme.onTertiary
                        else MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(stringResource(R.string.btn_close_board), fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // ⭐ Auto-focus first item when dialog opens
    LaunchedEffect(prizes) {
        if (prizes.isNotEmpty()) {
            firstItemFocusRequester.requestFocus()
        }
    }
}

@Composable
fun WinnerHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .background(
                color = PurpleDark,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.header_prize),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = stringResource(R.string.header_winner),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = stringResource(R.string.header_claim),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun WinnerItemRow(
    db :AppDatabase,
    gameId: String,
    prize: WinningPrizeEntity,
    suggestions: List<PlayerEntity>,
    focusRequester: FocusRequester? = null,
    onClaimToggle: suspend (Boolean) -> Unit,
    onReloadPlayers: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val isClaimed = prize.isClaimed
    var isFocused by remember { mutableStateOf(false) }
    var showUnclaimDialog by remember { mutableStateOf(false) }
// State for name editing
    var editedName by remember(prize.winnerName) { mutableStateOf(prize.winnerName ?: "") }
    var showDropdown by remember { mutableStateOf(false) }
    var showMultiSelectDialog by remember { mutableStateOf(false) }
    
    // Get keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current

    val isModerated = gameId.length == 6
    
    // Filter suggestions check
    val filteredSuggestions = remember(editedName, suggestions, isModerated) {
        if (isModerated) suggestions // Show all in dropdown for moderated
        else {
             if (editedName.isBlank()) emptyList()
             else suggestions.filter { it.name.startsWith(editedName, ignoreCase = true) && !it.name.equals(editedName, ignoreCase = true) }
        }
    }
    // Focus cycling
    val textFocusRequester = remember { focusRequester ?: FocusRequester() }
    val checkboxFocusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .onFocusChanged { isFocused = it.hasFocus } // Check hasFocus to detect child focus
            //.then(if (isModerated) Modifier.focusable() else Modifier) // Removed: children handle focus
            .background(
                color =
                    if (isFocused)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = prize.savedRule.ruleName.uppercase(),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color =
                    when {
                        isClaimed -> Color.Red
                        isFocused -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onPrimary
                    }
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Box {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = {
                        if (!isModerated) {
                            editedName = it
                            showDropdown = true
                        }
                    },
                    readOnly = isModerated, // Read-only in moderated mode
                    enabled = !isClaimed,
                    placeholder = { Text(if (isModerated) stringResource(R.string.placeholder_select_winners) else stringResource(R.string.placeholder_add_winner), color = Color.Gray) },
                    singleLine = false,
                    maxLines = 2,
                    trailingIcon = if (isModerated && !isClaimed) {
                        { 
                             // Clicking icon also opens dialog
                             androidx.compose.material3.IconButton(onClick = { showMultiSelectDialog = true }) {
                                 androidx.compose.material3.Icon(
                                     imageVector = androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                                     contentDescription = stringResource(R.string.desc_select)
                                 )
                             }
                        } 
                    } else null,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.Characters),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (!isModerated) {
                                scope.launch(Dispatchers.IO) {
                                    db.winningPrizeDao().updateWinnerName(prize.prizeId, editedName)
                                }
                                defaultKeyboardAction(ImeAction.Done)
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(textFocusRequester) // Always attach focus requester
                        // Handle D-pad
                        .onKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown) {
                                when (event.key) {
                                    Key.Enter, Key.NumPadEnter, Key.DirectionCenter -> {
                                        if (isModerated) {
                                             if (!isClaimed) showMultiSelectDialog = true
                                             true
                                        } else {
                                            keyboardController?.show()
                                            true
                                        }
                                    }
                                    Key.DirectionRight -> {
                                        checkboxFocusRequester.requestFocus()
                                        true
                                    }
                                    Key.DirectionLeft -> {
                                        checkboxFocusRequester.requestFocus() // Cycle to check as well? Or stay? Cycle makes sense if there are 2 items.
                                        true
                                    }
                                    else -> false
                                }
                            } else {
                                false
                            }
                        }
                        .onFocusChanged { focusState ->
                            if (!isModerated && !focusState.isFocused && editedName != prize.winnerName) {
                                scope.launch(Dispatchers.IO) {
                                    db.winningPrizeDao().updateWinnerName(prize.prizeId, editedName)
                                }
                            }
                        }
                        // If moderated, click should open dialog
                        .then(if (isModerated && !isClaimed) Modifier.clickable { showMultiSelectDialog = true } else Modifier),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        cursorColor = if (isModerated) Color.Transparent else Color.White
                    )
                )

                if (!isModerated) {
                    DropdownMenu(
                        expanded = showDropdown && filteredSuggestions.isNotEmpty() && !isClaimed,
                        onDismissRequest = { showDropdown = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                            .heightIn(max = 200.dp) // Limit height for scrolling
                    ) {
                        filteredSuggestions.forEach { player ->
                            DropdownMenuItem(
                                text = { Text(player.name, color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    editedName = player.name
                                    showDropdown = false
                                    scope.launch(Dispatchers.IO) {
                                        db.winningPrizeDao().updateWinnerName(prize.prizeId, editedName)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

        Checkbox(
            checked = isClaimed,
            modifier = Modifier
                .focusRequester(checkboxFocusRequester) // Attach focus requester
                .onKeyEvent { event ->
                     if (event.type == KeyEventType.KeyDown) {
                         when (event.key) {
                             Key.DirectionLeft, Key.DirectionRight -> {
                                 textFocusRequester.requestFocus()
                                 true
                             }
                             else -> false
                         }
                     } else false
                },
            colors = CheckboxDefaults.colors(
                checkedColor = if (isFocused) Color.White else Color(0xFF1E88E5), // Fix: use theme color if preferred, but keeping distinct highlight logic
                uncheckedColor = if (isFocused) Color.White else Color.Gray,
                checkmarkColor = if (isFocused) Color(0xFF1E88E5) else Color.White
            ),
            onCheckedChange = { checked ->
                if (!checked && isClaimed) {
                    showUnclaimDialog = true
                } else {
                    scope.launch { onClaimToggle(true) }
                }
            }
        )
    }

    // ---- Unclaim confirmation dialog (unchanged) ----
    if (showUnclaimDialog) {
        AlertDialog(
            onDismissRequest = { showUnclaimDialog = false },
            title = { Text(stringResource(R.string.dialog_unclaim_title), color = Color.Red) },
            text = { Text(stringResource(R.string.dialog_unclaim_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUnclaimDialog = false
                        scope.launch { onClaimToggle(false) }
                    }
                ) { Text(stringResource(R.string.dialog_yes)) }
            },
            dismissButton = {
                TextButton(onClick = { showUnclaimDialog = false }) {
                    Text(stringResource(R.string.dialog_no))
                }
            }
        )
    }

    //MultiSelectPlayerDialog
    if (showMultiSelectDialog && isModerated) {
        val currentSelections = remember(editedName) {
            if (editedName.isBlank()) emptySet()
            else editedName.split(",").map { it.trim() }.toSet()
        }
        
        MultiSelectPlayerDialog(
            allPlayers = suggestions,
            initialSelections = currentSelections,
            onDismiss = { showMultiSelectDialog = false },
            onConfirm = { selectedList ->
                val newName = selectedList.sorted().joinToString(", ")
                editedName = newName
                scope.launch(Dispatchers.IO) {
                    db.winningPrizeDao().updateWinnerName(prize.prizeId, newName)
                }
                showMultiSelectDialog = false
            },
            onReloadPlayers = onReloadPlayers
        )
    }
}

@Composable
fun MultiSelectPlayerDialog(
    allPlayers: List<PlayerEntity>,
    initialSelections: Set<String>,
    onDismiss: () -> Unit,
    onConfirm: (List<String>) -> Unit,
    onReloadPlayers: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val selectedNames = remember { mutableStateListOf<String>().apply { addAll(initialSelections) } }
    
    val filteredPlayers = remember(searchQuery, allPlayers) {
        if (searchQuery.isBlank()) allPlayers
        else allPlayers.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth().heightIn(max = 600.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.dialog_multi_select_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Row for Search + Reload
                Row(modifier = Modifier.fillMaxWidth().padding(bottom=8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(stringResource(R.string.placeholder_search_player), color = Color.Black) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        leadingIcon = {
                            androidx.compose.material3.Icon(
                                androidx.compose.material.icons.Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    androidx.compose.material3.IconButton(onClick = onReloadPlayers) {
                        androidx.compose.material3.Icon(
                             androidx.compose.material.icons.Icons.Default.Refresh,
                             contentDescription = stringResource(R.string.desc_reload_players),
                             tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Player List
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(filteredPlayers, key = { it.name }) { player ->
                        val isSelected = selectedNames.contains(player.name)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else Color.Transparent)
                                .clickable {
                                    if (isSelected) {
                                        selectedNames.remove(player.name)
                                    } else {
                                        selectedNames.add(player.name)
                                    }
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selectedNames.contains(player.name),
                                onCheckedChange = { checked ->
                                    if (checked) selectedNames.add(player.name)
                                    else selectedNames.remove(player.name)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = Color.White,
                                    checkmarkColor = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = player.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.btn_cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { onConfirm(selectedNames.toList()) }) {
                        Text(stringResource(R.string.btn_confirm))
                    }
                }
            }
        }
    }
}


@Composable
fun NumberGrid(calledNumbers: List<Int>, lastNumber: Int?, flashColor: Color) {
    val configuration = LocalConfiguration.current

    // Dynamically adjust cell size based on screen width/height to fit better
    val cellSize = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        (configuration.screenHeightDp * 0.075).dp // Slightly smaller for landscape
    } else {
        (configuration.screenWidthDp * 0.082).dp // Slightly smaller for portrait
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(10),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(4.dp),
        // CHANGE: Set to true to ensure all rows are reachable if they don't fit
        userScrollEnabled = true
    ) {
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
                    .size(cellSize)
                    .background(color = bgColor, shape = RoundedCornerShape(4.dp))
                    .border(1.dp, Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$number",
                    fontSize = (cellSize.value * 0.4).sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCalled || isLast) Color.White else MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }
}