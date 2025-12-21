package `in`.mahato.tambola.rule

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// THEME COLORS
val PurpleBgStart = Color(0xFF4A148C)
val PurpleBgEnd = Color(0xFF7B1FA2)
val GoldButton = Color(0xFFFFC107)
val MintButton = Color(0xFF1DE9B6)
val DPadFocusColor = Color.White

@Composable
fun PointDistributionScreen(ruleViewModel: RuleViewModel) {
    var isManualMode by remember { mutableStateOf(true) }
    var totalPoints by remember { mutableStateOf("1000") }

    val rules = ruleViewModel.selectedRules
    val totalPointsInt = totalPoints.toIntOrNull() ?: 0
    val scrollState = rememberScrollState()

    // Focus Requesters for D-Pad navigation
    val textFieldFocusRequester = remember { FocusRequester() }

    val allocatedPoints = rules.sumOf {
        ((it.percentage / 100.0) * totalPointsInt * it.quantity).toInt()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleBgStart, PurpleBgEnd)))
            .padding(16.dp)
    ) {
        Column {
            Text(
                "DISTRIBUTE POINTS",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            // 1. TOTAL POINTS INPUT
            var isFieldFocused by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = totalPoints,
                onValueChange = { totalPoints = it },
                label = { Text("TOTAL POINTS", color = if(isFieldFocused) Color.White else Color.LightGray) },
                textStyle = LocalTextStyle.current.copy(color = Color.White, fontWeight = FontWeight.Bold),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(textFieldFocusRequester)
                    .onFocusChanged { isFieldFocused = it.isFocused }
                    .border(
                        width = if (isFieldFocused) 2.dp else 1.dp,
                        color = if (isFieldFocused) DPadFocusColor else Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                    cursorColor = GoldButton,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(12.dp))

            // 2. MODE TOGGLE
            ModeToggleButton(
                isManual = isManualMode,
                onToggle = {
                    isManualMode = it
                    if (!it) ruleViewModel.autoDistribute()
                }
            )

            Spacer(Modifier.height(12.dp))

            // 3. MAIN CONTENT CARD (Rules + Summary)
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.25f))
            ) {
                Column(Modifier.padding(16.dp)) {
                    // Header
                    RulesHeader()
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f), thickness = 1.dp)

                    // Rules List (Scrollable)
                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {
                        rules.forEachIndexed { index, rule ->
                            RuleRow(
                                rule = rule,
                                totalPoints = totalPointsInt,
                                isManualMode = isManualMode,
                                onPercentChange = { ruleViewModel.updatePercentage(index, it) },
                                onQuantityChange = { ruleViewModel.updateQuantity(index, it, !isManualMode) }
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    // 4. SUMMARY (Now below the table)
                    SummaryCard(allocated = allocatedPoints, total = totalPointsInt)
                }
            }

            Spacer(Modifier.height(12.dp))

            // 5. CONFIRM BUTTON
            val canConfirm = allocatedPoints == totalPointsInt && totalPointsInt > 0
            var isConfirmFocused by remember { mutableStateOf(false) }

            Button(
                enabled = canConfirm,
                onClick = { /* Proceed Logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .onFocusChanged { isConfirmFocused = it.isFocused }
                    .border(
                        width = if (isConfirmFocused) 4.dp else 0.dp,
                        color = DPadFocusColor,
                        shape = RoundedCornerShape(100.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MintButton,
                    contentColor = Color.Black,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                )
            ) {
                Text("CONFIRM DISTRIBUTION", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }
        }
    }

    // Auto-focus TextField on screen entry
    LaunchedEffect(Unit) {
        textFieldFocusRequester.requestFocus()
    }
}

@Composable
fun SummaryCard(allocated: Int, total: Int) {
    val remaining = total - allocated
    val statusColor = if (remaining == 0) MintButton else Color(0xFFFF5252)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
    ) {
        Row(
            Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("SUMMARY", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.LightGray)
                Text("Total Used: $allocated", fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("REMAINING", fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.LightGray)
                Text(
                    text = remaining.toString(),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = statusColor
                )
            }
        }
    }
}

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rule.name,
            modifier = Modifier.weight(1.5f),
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        ValueStepper(
            valueText = "${rule.percentage}%",
            enabled = isManualMode,
            onMinus = { onPercentChange(-5) },
            onPlus = { onPercentChange(5) },
            modifier = Modifier.weight(1.8f)
        )

        ValueStepper(
            valueText = rule.quantity.toString(),
            enabled = true,
            onMinus = { onQuantityChange(-1) },
            onPlus = { onQuantityChange(1) },
            modifier = Modifier.weight(1.5f)
        )

        Text(
            text = amount.toString(),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            color = GoldButton,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ValueStepper(
    valueText: String,
    enabled: Boolean,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        StepperIconButton(Icons.Default.Remove, enabled, onMinus)
        Text(
            valueText,
            modifier = Modifier.width(44.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = if (enabled) Color.White else Color.Gray
        )
        StepperIconButton(Icons.Default.Add, enabled, onPlus)
    }
}

@Composable
fun StepperIconButton(icon: ImageVector, enabled: Boolean, onClick: () -> Unit) {
    var isFocused by remember { mutableStateOf(false) }

    IconButton(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .background(
                color = if (isFocused) Color.White.copy(alpha = 0.2f) else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = DPadFocusColor,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Icon(icon, null, tint = if (enabled) Color.White else Color.Gray, modifier = Modifier.size(18.dp))
    }
}

@Composable
fun ModeToggleButton(isManual: Boolean, onToggle: (Boolean) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val color by animateColorAsState(if (isManual) GoldButton else MintButton, label = "color")

    Button(
        onClick = { onToggle(!isManual) },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .border(
                width = if (isFocused) 4.dp else 0.dp,
                color = DPadFocusColor,
                shape = RoundedCornerShape(100.dp)
            ),
        colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.Black)
    ) {
        Text(
            if (isManual) "MODE: MANUAL" else "MODE: AUTOMATIC",
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun RulesHeader() {
    Row(Modifier.padding(bottom = 8.dp)) {
        val style = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.LightGray)
        Text("RULE", Modifier.weight(1.5f), style = style)
        Text("%", Modifier.weight(1.8f), style = style, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Text("QTY", Modifier.weight(1.5f), style = style, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        Text("AMT", Modifier.weight(1f), style = style, textAlign = androidx.compose.ui.text.style.TextAlign.End)
    }
}