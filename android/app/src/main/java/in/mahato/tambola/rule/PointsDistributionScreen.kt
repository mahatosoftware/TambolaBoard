package `in`.mahato.tambola.rule

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import `in`.mahato.tambola.game.model.TambolaRule
import `in`.mahato.tambola.rule.viewmodel.RuleViewModel


/* ---------------- COLORS ---------------- */

val PurpleBgStart = Color(0xFF4A148C)
val PurpleBgEnd = Color(0xFF7B1FA2)
val GoldButton = Color(0xFFFFC107)
val MintButton = Color(0xFF1DE9B6)
val DPadFocusColor = Color.White


/* ---------------- MAIN SCREEN ---------------- */

@Composable
fun PointDistributionScreen(ruleViewModel: RuleViewModel) {

    var isManualMode by remember { mutableStateOf(false) }
    var totalPoints by remember { mutableStateOf("1000") }

    val rules = ruleViewModel.selectedRules
    val totalPointsInt = totalPoints.toIntOrNull() ?: 0
    val allocatedPoints = rules.sumOf {
        ((it.percentage / 100.0) * totalPointsInt * it.quantity).toInt()
    }

    val scrollState = rememberScrollState()
    val isTv = isTvDevice()
    val focusRequester = remember { FocusRequester() }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleBgStart, PurpleBgEnd)))
            .padding(16.dp)
    ) {
        Column {

            Text(
                "DISTRIBUTE POINTS",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = totalPoints,
                onValueChange = { totalPoints = it },
                label = { Text("TOTAL POINTS", ) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                colors = TextFieldDefaults.colors(
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    disabledLabelColor = Color.LightGray,
                    errorLabelColor = Color.Red,
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                    cursorColor = GoldButton,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(Modifier.height(12.dp))

            OverlappingModeSelector(
                isManual = isManualMode,
                onModeChange = {
                    isManualMode = it
                    if (!it) ruleViewModel.autoDistribute()
                }
            )

            Spacer(Modifier.height(12.dp))

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.25f)
                )
            ) {

                if (isTv) {
                    Row(
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

                        Column(
                            Modifier
                                .weight(3f)
                                .fillMaxHeight()
                        ) {
                            RulesHeader()
                            HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                            Column(
                                Modifier
                                    .weight(1f)
                                    .verticalScroll(scrollState)
                            ) {
                                rules.forEachIndexed { index, rule ->
                                    RuleRow(
                                        rule,
                                        totalPointsInt,
                                        isManualMode,
                                        { ruleViewModel.updatePercentage(index, it) },
                                        { ruleViewModel.updateQuantity(index, it, !isManualMode) }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        Column(
                            Modifier
                                .weight(1.2f)
                                .fillMaxHeight()
                        ) {
                            SummaryCard(
                                allocated = allocatedPoints,
                                total = totalPointsInt,
                                rules

                            )
                        }
                    }
                } else {

                    Column(Modifier.padding(16.dp)) {

                        RulesHeader()
                        HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                        Column(
                            Modifier
                                .weight(1f)
                                .verticalScroll(scrollState)
                        ) {
                            rules.forEachIndexed { index, rule ->
                                RuleRow(
                                    rule,
                                    totalPointsInt,
                                    isManualMode,
                                    { ruleViewModel.updatePercentage(index, it) },
                                    { ruleViewModel.updateQuantity(index, it, !isManualMode) }
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        SummaryCard(
                            allocated = allocatedPoints,
                            total = totalPointsInt,rules

                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            val context = LocalContext.current
            var confirmDistributionFocused by remember { mutableStateOf(false) }
            Button(
                enabled = allocatedPoints == totalPointsInt && totalPointsInt > 0,
                onClick = {
                    val intent = Intent(context, SummaryActivity::class.java).apply {
                        // Ensure TambolaRule implements Parcelable
                        putParcelableArrayListExtra("rules", ArrayList(rules))
                        putExtra("totalPoints", totalPointsInt)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .onFocusChanged { confirmDistributionFocused = it.isFocused }
                    .height(60.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = if (confirmDistributionFocused)
                        MaterialTheme.colorScheme.background
                    else MaterialTheme.colorScheme.primaryContainer,
                    contentColor = if (confirmDistributionFocused)
                        MaterialTheme.colorScheme.onTertiary
                    else MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text("CONFIRM DISTRIBUTION", fontWeight = FontWeight.ExtraBold)
            }
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

/* ---------------- MODE SELECTOR ---------------- */

@Composable
fun OverlappingModeSelector(
    isManual: Boolean,
    onModeChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {

        ModeButton(
            text = "AUTOMATIC",
            selected = !isManual,
            backgroundColor = MintButton,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .align(Alignment.CenterStart)
                .zIndex(if (!isManual) 2f else 1f),
            onClick = { onModeChange(false) }
        )

        ModeButton(
            text = "MANUAL",
            selected = isManual,
            backgroundColor = GoldButton,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .align(Alignment.CenterEnd)
                .zIndex(if (isManual) 2f else 1f),
            onClick = { onModeChange(true) }
        )
    }
}

@Composable
fun ModeButton(
    text: String,
    selected: Boolean,
    backgroundColor: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        if (selected) backgroundColor else Color.Black.copy(alpha = 0.35f),
        label = "modeColor"
    )

    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(100.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = if (selected) Color.Black else Color.White
        )
    ) {
        Text(text, fontWeight = FontWeight.ExtraBold)
    }
}

/* ---------------- SUMMARY CARD ---------------- */


@Composable
fun SummaryCard(
    allocated: Int,
    total: Int,
    rules: List<TambolaRule>
) {
    val remaining = total - allocated
    val color = if (remaining == 0) MintButton else Color.Red
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // Launch SummaryActivity
                val intent = Intent(context, SummaryActivity::class.java).apply {
                    putParcelableArrayListExtra("rules", ArrayList(rules))
                    putExtra("totalPoints", total)
                    putExtra("allocatedPoints", allocated)
                }
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {
                Text(
                    text = "SUMMARY",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Used: $allocated",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "REMAINING",
                    fontSize = 10.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = remaining.toString(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = color
                )
            }
        }
    }
}


/* ---------------- RULE ROW ---------------- */

@Composable
fun RuleRow(
    rule: TambolaRule,
    totalPoints: Int,
    isManualMode: Boolean,
    onPercentChange: (Int) -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    val amount = ((rule.percentage / 100.0) * totalPoints * rule.quantity).toInt()

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(rule.name, Modifier.weight(1.5f), color = Color.White)

        ValueStepper(
            "${rule.percentage}%",
            isManualMode,
            { onPercentChange(-1) },
            { onPercentChange(1) },
            Modifier.weight(1.5f)
        )

        ValueStepper(
            rule.quantity.toString(),
            true,
            { onQuantityChange(-1) },
            { onQuantityChange(1) },
            Modifier.weight(1.2f)
        )

        Text(
            amount.toString(),
            Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = GoldButton,
            fontWeight = FontWeight.Bold
        )
    }
}

/* ---------------- STEPPER ---------------- */

@Composable
fun ValueStepper(
    value: String,
    enabled: Boolean,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier
) {
    Row(modifier = modifier.widthIn(min = 120.dp), verticalAlignment = Alignment.CenterVertically) {
        StepperIconButton(Icons.Default.Remove, enabled, onMinus)
        Text(value, Modifier.width(32.dp), textAlign = TextAlign.Center)
        StepperIconButton(Icons.Default.Add, enabled, onPlus)
    }
}

@Composable
fun StepperIconButton(icon: ImageVector, enabled: Boolean, onClick: () -> Unit) {
    IconButton(enabled = enabled, onClick = onClick,modifier = Modifier.size(32.dp)) {
        Icon(icon, null, tint = if (enabled) Color.White else Color.Gray)
    }
}

/* ---------------- HEADER ---------------- */

@Composable
fun RulesHeader() {
    Row(Modifier.padding(bottom = 8.dp)) {
        val style = LocalTextStyle.current.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = Color.LightGray
        )
        Text("RULE", Modifier.weight(1.5f), style = style)
        Text("PERCENT(%)", Modifier.weight(1.5f), style = style, textAlign = TextAlign.Center)
        Text("QUANTITY", Modifier.weight(1.2f), style = style, textAlign = TextAlign.Center)
        Text("AMOUNT", Modifier.weight(1f), style = style, textAlign = TextAlign.End)
    }
}
