package `in`.mahato.tambola.rule

import android.content.Intent

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import android.widget.Toast
import `in`.mahato.tambola.MainActivity
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import `in`.mahato.tambola.game.model.TambolaRule
import `in`.mahato.tambola.rule.viewmodel.RuleViewModel
import `in`.mahato.tambola.util.GeneralUtil


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
    val totalPoints = "1000"

    val rules = ruleViewModel.selectedRules
    val totalPointsInt = totalPoints.toIntOrNull() ?: 0

    val scrollState = rememberScrollState()

    Box(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleBgStart, PurpleBgEnd)))
            .padding(16.dp)
            .padding(WindowInsets.safeDrawing.asPaddingValues())
    ) {
        Column {

            Text(
                "DISTRIBUTE POINTS",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.25f)
                )
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    RulesHeader()
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))

                    Column(
                        Modifier
                            .weight(1f)
                            .verticalScroll(scrollState)
                    ) {
                        for ((index, rule) in rules.withIndex()) {
                            RuleRow(
                                rule,
                                { ruleViewModel.updateQuantity(index, it, true) }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            val context = LocalContext.current
            var confirmDistributionFocused by remember { mutableStateOf(false) }
            Button(
                enabled = totalPointsInt > 0,
                onClick = {
                    val totalPointsInt = 1000 // Fixed value as per previous refactor
                    ruleViewModel.saveRules(totalPointsInt) {
                        Toast.makeText(context, "Rules Saved Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
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
                Text("Save Rules", fontWeight = FontWeight.ExtraBold)
            }
            Text(
                text = GeneralUtil.getCopyrightMessage(),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }


}




/* ---------------- RULE ROW ---------------- */

@Composable
fun RuleRow(
    rule: TambolaRule,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(rule.name, Modifier.weight(1.5f), color = Color.White)

        ValueStepper(
            rule.quantity.toString(),
            true,
            { onQuantityChange(-1) },
            { onQuantityChange(1) },
            Modifier.weight(1.2f)
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
        Text("QUANTITY", Modifier.weight(1.2f), style = style, textAlign = TextAlign.Center)
    }
}
