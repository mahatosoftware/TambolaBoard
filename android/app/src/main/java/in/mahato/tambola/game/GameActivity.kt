package `in`.mahato.tambola.game

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
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
import androidx.compose.foundation.focusable
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
                tts.speak("Welcome to Tambola Board", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
        enableEdgeToEdge()
        setContent {
            var showWinnerBoard by remember { mutableStateOf(false) }
            AppTheme {
                TambolaScreen(db, tts, isNewGame, _isTtsReady.value)
            }

            if (showWinnerBoard) {
                WinnerBoardDialog(
                    db = AppDatabase.getDatabase(this),
                    onDismiss = { showWinnerBoard = false }
                )
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

    fun generateId() = (1..5).map { "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".random() }.joinToString("")

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
            val newId = generateId()
            dao.saveGameMetadata(GameMetadata(gameId = newId))
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
        val newId = generateId()
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
            CenterAlignedTopAppBar(
                title = { Text("Tambola Board", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /** MAIN CONTENT **/
            if (isLandscape) {
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
                            gameId
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
                        gameId
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
    gameId: String
) {
    var showResetDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }
    var showWinnerBoard by remember { mutableStateOf(false) }

    val frCall = remember { FocusRequester() }
    val frAuto = remember { FocusRequester() }
    val frWinner = remember { FocusRequester() }
    val frReset = remember { FocusRequester() }
    val frExit = remember { FocusRequester() }

    val requesters = remember { listOf(frCall, frAuto, frWinner, frReset, frExit) }
    var focusedIndex by remember { mutableIntStateOf(0) }

    fun handleDpad(event: KeyEvent, current: Int): Boolean {
        if (event.type != KeyEventType.KeyDown) return false
        when (event.key) {
            DirectionRight -> { requesters[(current + 1) % 5].requestFocus(); return true }
            DirectionLeft -> { requesters[(current - 1 + 5) % 5].requestFocus(); return true }
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
                        contentDescription = "Game QR",
                        modifier = Modifier
                            .size(if (isLandscape) 60.dp else 90.dp)
                            .background(Color.White)
                            .padding(4.dp)
                            .border(1.dp, Color.Black)
                    )
                }
                Text(
                    "GAME ID: $gameId",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        Card(
            modifier = Modifier.padding(vertical = if (isLandscape) 4.dp else 8.dp).fillMaxWidth(if (isLandscape) 0.8f else 0.7f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("LAST NUMBER", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = "${lastNumber ?: "--"}",
                    style = if (isLandscape) MaterialTheme.typography.displayMedium else MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
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

                        Button(onClick = onCall, colors = getBtnColors(0), modifier = Modifier.padding(4.dp).focusRequester(frCall).onFocusChanged { if (it.isFocused) focusedIndex = 0 }.onKeyEvent { handleDpad(it, 0) }) { Text("Call Next") }
                        Button(onClick = onAutoToggle, colors = getBtnColors(1), modifier = Modifier.padding(4.dp).focusRequester(frAuto).onFocusChanged { if (it.isFocused) focusedIndex = 1 }.onKeyEvent { handleDpad(it, 1) }) { Text(if (isAutoCalling) "Pause" else "Auto Call") }
                        Button(onClick = { showWinnerBoard = true }, colors = getBtnColors(2), modifier = Modifier.padding(4.dp).focusRequester(frWinner).onFocusChanged { if (it.isFocused) focusedIndex = 2 }.onKeyEvent { handleDpad(it, 2) }) { Text("Claim Prize") }
                        Button(onClick = { showResetDialog = true }, colors = getBtnColors(3), modifier = Modifier.padding(4.dp).focusRequester(frReset).onFocusChanged { if (it.isFocused) focusedIndex = 3 }.onKeyEvent { handleDpad(it, 3) }) { Text("Reset") }
                        Button(onClick = { showExitDialog = true }, colors = getBtnColors(4), modifier = Modifier.padding(4.dp).focusRequester(frExit).onFocusChanged { if (it.isFocused) focusedIndex = 4 }.onKeyEvent { handleDpad(it, 4) }) { Text("Return to Main Menu") }
                    }
                } else {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }

    if (showWinnerBoard) {
        WinnerBoardDialog(db = db, onDismiss = { showWinnerBoard = false })
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
            text = { Text("Are you sure you want to return to the main Menu?") },
            confirmButton = { TextButton(onClick = { onBackClick(); showExitDialog = false }) { Text("Yes") } },
            dismissButton = { TextButton(onClick = { showExitDialog = false }) { Text("No") } }
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
    onDismiss: () -> Unit
) {
    val prizes by db.winningPrizeDao()
        .getAllPrizes()
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

                Text(
                    "Winner Board",
                    fontWeight = FontWeight.Black,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    "Claim prizes as they are won. Claimed prizes will appear at the Bottom",
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
                                prize = prize,
                                focusRequester =
                                    if (index == 0) firstItemFocusRequester else null,
                                onClaimToggle = { claimed ->
                                    if (claimed)
                                        db.winningPrizeDao().claimPrize(prize.prizeId)
                                    else
                                        db.winningPrizeDao().unclaimPrize(prize.prizeId)
                                }
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
                    Text("Close Board", fontWeight = FontWeight.Bold)
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
            text = "PRIZE",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = "WINNER",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = "CLAIM",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun WinnerItemRow(
    db :AppDatabase,
    prize: WinningPrizeEntity,
    focusRequester: FocusRequester? = null,
    onClaimToggle: suspend (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val isClaimed = prize.isClaimed
    var isFocused by remember { mutableStateOf(false) }
    var showUnclaimDialog by remember { mutableStateOf(false) }
// State for name editing
    var editedName by remember(prize.winnerName) { mutableStateOf(prize.winnerName ?: "") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .then(
                if (focusRequester != null)
                    Modifier.focusRequester(focusRequester)
                else Modifier
            )
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
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
            // ⭐ TV-Optimized TextField
            OutlinedTextField(
                value = editedName,
                onValueChange = {
                    // Update local UI only - this won't trigger a DB refresh
                    editedName = it
                },
                enabled = !isClaimed,
                placeholder = { Text("Add Winner Name...", color = Color.Gray) },
                singleLine = false,
                maxLines = 2,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Save to DB ONLY when "Done" is pressed on the virtual keyboard
                        scope.launch(Dispatchers.IO) {
                            db.winningPrizeDao().updateWinnerName(prize.prizeId, editedName)
                        }
                        // This will hide the keyboard automatically
                        defaultKeyboardAction(ImeAction.Done)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        // OPTIONAL: Save when the user navigates the D-pad away from the text box
                        if (!focusState.isFocused && editedName != prize.winnerName) {
                            scope.launch(Dispatchers.IO) {
                                db.winningPrizeDao().updateWinnerName(prize.prizeId, editedName)
                            }
                        }
                    },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    cursorColor = Color.White
                )
            )
        }

        Checkbox(
            checked = isClaimed,
            colors = CheckboxDefaults.colors(
                checkedColor = if (isFocused) Color.White else Color(0xFF1E88E5),
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
            title = { Text("Confirm", color = Color.Red) },
            text = { Text("Are you sure you want to unclaim the Prize?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showUnclaimDialog = false
                        scope.launch { onClaimToggle(false) }
                    }
                ) { Text("Yes") }
            },
            dismissButton = {
                TextButton(onClick = { showUnclaimDialog = false }) {
                    Text("No")
                }
            }
        )
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