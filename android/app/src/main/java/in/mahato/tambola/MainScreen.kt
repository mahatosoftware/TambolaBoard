package `in`.mahato.tambola

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.mahato.tambola.util.GeneralUtil
import `in`.mahato.tambola.util.ScreenSizeUtil
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSelectGameRule: () -> Unit,
    onNewGame: () -> Unit,
    onContinue: () -> Unit,
    onViewWinners: () -> Unit,
    onExit: () -> Unit
) {
    val context = LocalContext.current
    val config = LocalConfiguration.current
    val isTv = ScreenSizeUtil.isTv(context)
    val isWideScreen = isTv || config.screenWidthDp >= 600 || config.orientation == Configuration.ORIENTATION_LANDSCAPE

    val focusNew = remember { FocusRequester() }
    val focusContinue = remember { FocusRequester() }
    val focusViewWinners = remember { FocusRequester() }
    val focusHowToPlay = remember { FocusRequester() }
    val focusSettings = remember { FocusRequester() }
    val focusExit = remember { FocusRequester() }

    var showSettingsDialog by remember { mutableStateOf(false) }
    var showHowToPlayDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusNew.requestFocus()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Title
            Text(
                text = stringResource(R.string.main_welcome_message),
                fontSize = if (isWideScreen) 24.sp else 20.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isWideScreen) {
                // ----------------------------------------------------
                // TV / TABLET DASHBOARD LAYOUT (Option 2: Hero + 2x2 Grid)
                // ----------------------------------------------------
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.95f),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column: Hero Cards (45% Width)
                    Column(
                        modifier = Modifier
                            .weight(0.45f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Hero 1: Start New Game
                        DashboardTile(
                            text = stringResource(R.string.btn_start_new_game),
                            onClick = onNewGame,
                            focusRequester = focusNew,
                            isHero = true,
                            onDirectionalKey = { key ->
                                when (key) {
                                    Key.DirectionDown -> { focusContinue.requestFocus(); true }
                                    Key.DirectionRight -> { focusViewWinners.requestFocus(); true }
                                    else -> false
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )

                        // Hero 2: Continue Last Game
                        DashboardTile(
                            text = stringResource(R.string.btn_continue_last_game),
                            onClick = onContinue,
                            focusRequester = focusContinue,
                            isHero = true,
                            onDirectionalKey = { key ->
                                when (key) {
                                    Key.DirectionUp -> { focusNew.requestFocus(); true }
                                    Key.DirectionRight -> { focusSettings.requestFocus(); true }
                                    else -> false
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Right Column: 2x2 Action Grid (55% Width)
                    Column(
                        modifier = Modifier
                            .weight(0.55f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Row 1: View Winners & How to Play
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DashboardTile(
                                text = stringResource(R.string.btn_view_winners),
                                onClick = onViewWinners,
                                focusRequester = focusViewWinners,
                                onDirectionalKey = { key ->
                                    when (key) {
                                        Key.DirectionLeft -> { focusNew.requestFocus(); true }
                                        Key.DirectionRight -> { focusHowToPlay.requestFocus(); true }
                                        Key.DirectionDown -> { focusSettings.requestFocus(); true }
                                        else -> false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            DashboardTile(
                                text = stringResource(R.string.btn_how_to_play),
                                onClick = { showHowToPlayDialog = true },
                                focusRequester = focusHowToPlay,
                                onDirectionalKey = { key ->
                                    when (key) {
                                        Key.DirectionLeft -> { focusViewWinners.requestFocus(); true }
                                        Key.DirectionDown -> { focusExit.requestFocus(); true }
                                        else -> false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        // Row 2: Settings & Exit
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DashboardTile(
                                text = stringResource(R.string.btn_settings),
                                onClick = { showSettingsDialog = true },
                                focusRequester = focusSettings,
                                onDirectionalKey = { key ->
                                    when (key) {
                                        Key.DirectionLeft -> { focusContinue.requestFocus(); true }
                                        Key.DirectionRight -> { focusExit.requestFocus(); true }
                                        Key.DirectionUp -> { focusViewWinners.requestFocus(); true }
                                        else -> false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )

                            DashboardTile(
                                text = stringResource(R.string.btn_exit),
                                onClick = onExit,
                                focusRequester = focusExit,
                                onDirectionalKey = { key ->
                                    when (key) {
                                        Key.DirectionLeft -> { focusSettings.requestFocus(); true }
                                        Key.DirectionUp -> { focusHowToPlay.requestFocus(); true }
                                        else -> false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            } else {
                // ----------------------------------------------------
                // MOBILE PORTRAIT SINGLE COLUMN LAYOUT
                // ----------------------------------------------------
                val focusRequesters = listOf(focusNew, focusContinue, focusViewWinners, focusHowToPlay, focusSettings, focusExit)
                var focusedIndex by remember { mutableStateOf(0) }

                fun moveFocus(up: Boolean) {
                    focusedIndex = if (up) {
                        (focusedIndex - 1 + focusRequesters.size) % focusRequesters.size
                    } else {
                        (focusedIndex + 1) % focusRequesters.size
                    }
                    focusRequesters[focusedIndex].requestFocus()
                }

                val buttonWidth = config.screenWidthDp.dp * 0.8f

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Start New Game
                    MobileButton(
                        text = stringResource(R.string.btn_start_new_game),
                        onClick = onNewGame,
                        focusRequester = focusNew,
                        width = buttonWidth,
                        onMoveFocus = ::moveFocus
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Continue Last Game
                    MobileButton(
                        text = stringResource(R.string.btn_continue_last_game),
                        onClick = onContinue,
                        focusRequester = focusContinue,
                        width = buttonWidth,
                        onMoveFocus = ::moveFocus
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // View Winners
                    MobileButton(
                        text = stringResource(R.string.btn_view_winners),
                        onClick = onViewWinners,
                        focusRequester = focusViewWinners,
                        width = buttonWidth,
                        onMoveFocus = ::moveFocus
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // How to Play
                    MobileButton(
                        text = stringResource(R.string.btn_how_to_play),
                        onClick = { showHowToPlayDialog = true },
                        focusRequester = focusHowToPlay,
                        width = buttonWidth,
                        onMoveFocus = ::moveFocus
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Settings
                    MobileButton(
                        text = stringResource(R.string.btn_settings),
                        onClick = { showSettingsDialog = true },
                        focusRequester = focusSettings,
                        width = buttonWidth,
                        onMoveFocus = ::moveFocus
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Exit
                    MobileButton(
                        text = stringResource(R.string.btn_exit),
                        onClick = onExit,
                        focusRequester = focusExit,
                        width = buttonWidth,
                        onMoveFocus = ::moveFocus
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Copyright Notice
            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }

    if (showHowToPlayDialog) {
        `in`.mahato.tambola.ui.HowToPlayDialog(
            onDismiss = { showHowToPlayDialog = false }
        )
    }

    if (showSettingsDialog) {
        `in`.mahato.tambola.ui.LanguageSettingsDialog(
            onDismiss = { showSettingsDialog = false },
            onLanguageChanged = { _ -> }
        )
    }
}

@Composable
fun DashboardTile(
    text: String,
    onClick: () -> Unit,
    focusRequester: FocusRequester,
    onDirectionalKey: (Key) -> Boolean,
    modifier: Modifier = Modifier,
    isHero: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isFocused) 1.05f else 1.0f, label = "scale")

    val containerColor = when {
        isFocused -> MaterialTheme.colorScheme.background
        isHero -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.75f)
    }

    val contentColor = when {
        isFocused -> MaterialTheme.colorScheme.onTertiary
        else -> MaterialTheme.colorScheme.tertiary
    }

    val borderColor = if (isFocused) MaterialTheme.colorScheme.onTertiary else Color.Transparent

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = containerColor,
        contentColor = contentColor,
        border = BorderStroke(if (isFocused) 3.dp else 0.dp, borderColor),
        tonalElevation = if (isFocused) 12.dp else 4.dp,
        modifier = modifier
            .scale(scale)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    onDirectionalKey(event.key)
                } else false
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontWeight = if (isFocused || isHero) FontWeight.ExtraBold else FontWeight.Bold,
                fontSize = if (isHero) 20.sp else 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp
            )
        }
    }
}

@Composable
fun MobileButton(
    text: String,
    onClick: () -> Unit,
    focusRequester: FocusRequester,
    width: androidx.compose.ui.unit.Dp,
    onMoveFocus: (Boolean) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Button(
        onClick = onClick,
        modifier = Modifier
            .defaultMinSize(minWidth = 280.dp, minHeight = 50.dp)
            .width(width)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.DirectionDown -> { onMoveFocus(false); true }
                        Key.DirectionUp -> { onMoveFocus(true); true }
                        else -> false
                    }
                } else false
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFocused)
                MaterialTheme.colorScheme.background
            else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (isFocused)
                MaterialTheme.colorScheme.onTertiary
            else MaterialTheme.colorScheme.tertiary
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}
