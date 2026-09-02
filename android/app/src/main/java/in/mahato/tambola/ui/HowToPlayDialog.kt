package `in`.mahato.tambola.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import `in`.mahato.tambola.R
import `in`.mahato.tambola.util.ScreenSizeUtil
import kotlinx.coroutines.delay

// High-contrast color constants for dark purple TV surface
private val DialogBgColor = Color(0xFF3C0084)          // Deep Royal Purple
private val CardBgDark = Color(0xFF1E0738)             // Darker Contrast Purple
private val InnerCardBg = Color(0xFF2B104D)            // Medium Contrast Purple
private val EmeraldGreen = Color(0xFF00E676)           // Bright Emerald Green
private val ShamrockGreen = Color(0xFF00C853)          // Darker Accent Green
private val AccentGold = Color(0xFFFFD700)             // Bright Gold/Yellow
private val AccentPurple = Color(0xFFE1BEE7)           // Bright Deep Lavender Purple
private val SoftPurpleBorder = Color(0xFF9C27B0)       // Purple Border

@Composable
fun HowToPlayDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isTv = remember { ScreenSizeUtil.isTv(context) }

    val firstCardFocusRequester = remember { FocusRequester() }
    val closeFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(100)
        firstCardFocusRequester.requestFocus()
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = DialogBgColor,
            tonalElevation = 12.dp,
            modifier = Modifier
                .fillMaxHeight(0.92f)
                .widthIn(max = 620.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // HEADER
                Text(
                    text = stringResource(R.string.title_how_to_play),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.subtitle_how_to_play),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFFE0E0E0)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // SCROLLABLE CONTENT FOR TV & MOBILE
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // SECTION 1: CHOOSE GAME MODE
                    item {
                        StepHeader(stepNumber = "1", title = stringResource(R.string.step_1_title))
                    }

                    // MODERATED MODE CARD (PROMINENT / RECOMMENDED)
                    item {
                        ModeratedModeCard(modifier = Modifier.focusRequester(firstCardFocusRequester))
                    }

                    // UNMODERATED MODE CARD
                    item {
                        UnmoderatedModeCard()
                    }

                    // SECTION 2: SET WINNING RULES & PRIZES
                    item {
                        StepHeader(stepNumber = "2", title = stringResource(R.string.step_2_title))
                    }
                    item {
                        StepSectionCard(
                            description = stringResource(R.string.step_2_desc)
                        )
                    }

                    // SECTION 3: CALL NUMBERS
                    item {
                        StepHeader(stepNumber = "3", title = stringResource(R.string.step_3_title))
                    }
                    item {
                        StepSectionCard(
                            description = stringResource(R.string.step_3_desc)
                        )
                    }

                    // SECTION 4: CLAIM & VERIFY WINNERS
                    item {
                        StepHeader(stepNumber = "4", title = stringResource(R.string.step_4_title))
                    }
                    item {
                        StepSectionCard(
                            description = stringResource(R.string.step_4_desc)
                        )
                    }

                    // SECTION 5: VIEW & SHARE WINNERS (PDF description adapts for TV vs Mobile)
                    item {
                        StepHeader(stepNumber = "5", title = stringResource(R.string.step_5_title))
                    }
                    item {
                        StepSectionCard(
                            description = stringResource(
                                if (isTv) R.string.step_5_desc_tv else R.string.step_5_desc_mobile
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // FOOTER CLOSE BUTTON
                var isCloseFocused by remember { mutableStateOf(false) }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .focusRequester(closeFocusRequester)
                        .onFocusChanged { isCloseFocused = it.isFocused },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCloseFocused) Color.White else ShamrockGreen,
                        contentColor = if (isCloseFocused) Color.Black else Color.White
                    ),
                    border = if (isCloseFocused) BorderStroke(3.dp, AccentGold) else null,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.btn_close),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------
 * SECTION HEADER COMPOSABLE
 * ------------------------------------------------------------------------- */
@Composable
fun StepHeader(stepNumber: String, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(
                    color = AccentGold,
                    shape = RoundedCornerShape(17.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 19.sp,
            color = Color.White
        )
    }
}

/* -------------------------------------------------------------------------
 * MODERATED MODE CARD (PROMINENT / RECOMMENDED)
 * ------------------------------------------------------------------------- */
@Composable
fun ModeratedModeCard(modifier: Modifier = Modifier) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.02f else 1.0f, label = "cardScale")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBgDark
        ),
        border = if (isFocused)
            BorderStroke(3.5.dp, AccentGold)
        else
            BorderStroke(2.dp, EmeraldGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 10.dp else 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // PROMINENT BADGE
            Surface(
                color = ShamrockGreen,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.badge_recommended_app),
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            Text(
                text = stringResource(R.string.mode_moderated_title),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = EmeraldGreen
            )
            Text(
                text = stringResource(R.string.mode_moderated_subtitle),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = AccentGold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.mode_moderated_desc),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // SCENARIO 1: DIGITAL TICKETS
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = InnerCardBg
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = stringResource(R.string.scenario_digital_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AccentPurple
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = stringResource(R.string.scenario_digital_desc),
                        fontSize = 13.sp,
                        color = Color(0xFFF5F5F5),
                        lineHeight = 18.sp
                    )
                }
            }

            // SCENARIO 2: PAPER TICKETS
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = InnerCardBg
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = stringResource(R.string.scenario_paper_title),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = AccentGold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = stringResource(R.string.scenario_paper_desc),
                        fontSize = 13.sp,
                        color = Color(0xFFF5F5F5),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // PROMINENT HELPER BANNER
            Surface(
                color = ShamrockGreen,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.rule_moderated_banner),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // PLAY STORE LINK BUTTON
            val context = LocalContext.current
            var isGetAppFocused by remember { mutableStateOf(false) }
            Button(
                onClick = { `in`.mahato.tambola.util.GeneralUtil.openPlayStore(context) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isGetAppFocused) Color.White else AccentGold,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp),
                border = if (isGetAppFocused) BorderStroke(3.dp, Color.White) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isGetAppFocused = it.isFocused }
                    .focusable()
            ) {
                Text(
                    text = stringResource(R.string.btn_get_tickets_app),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/* -------------------------------------------------------------------------
 * UNMODERATED MODE CARD
 * ------------------------------------------------------------------------- */
@Composable
fun UnmoderatedModeCard() {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.02f else 1.0f, label = "cardScale")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBgDark
        ),
        border = if (isFocused)
            BorderStroke(3.5.dp, AccentGold)
        else
            BorderStroke(1.5.dp, SoftPurpleBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 8.dp else 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.mode_unmoderated_title),
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = Color(0xFFE040FB)
            )
            Text(
                text = stringResource(R.string.mode_unmoderated_subtitle),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color(0xFFCE93D8)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.mode_unmoderated_desc),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // BULLET POINTS
            Column(
                modifier = Modifier.padding(start = 4.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(stringResource(R.string.unmoderated_bullet_1), fontSize = 13.5.sp, color = Color(0xFFF5F5F5))
                Text(stringResource(R.string.unmoderated_bullet_2), fontSize = 13.5.sp, color = Color(0xFFF5F5F5))
                Text(stringResource(R.string.unmoderated_bullet_3), fontSize = 13.5.sp, color = Color(0xFFF5F5F5))
                Text(stringResource(R.string.unmoderated_bullet_4), fontSize = 13.5.sp, color = Color(0xFFF5F5F5))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // HELPER BANNER
            Surface(
                color = InnerCardBg,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFCE93D8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.rule_unmoderated_banner),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

/* -------------------------------------------------------------------------
 * REUSABLE STEP SECTION CARD
 * ------------------------------------------------------------------------- */
@Composable
fun StepSectionCard(description: String) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isFocused) 1.02f else 1.0f, label = "stepCardScale")

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = InnerCardBg
        ),
        border = if (isFocused)
            BorderStroke(3.dp, AccentGold)
        else
            BorderStroke(1.dp, SoftPurpleBorder.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isFocused) 6.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
    ) {
        Box(modifier = Modifier.padding(14.dp)) {
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            )
        }
    }
}
