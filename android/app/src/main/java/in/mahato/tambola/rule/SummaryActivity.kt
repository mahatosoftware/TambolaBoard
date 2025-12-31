package `in`.mahato.tambola.rule

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import `in`.mahato.tambola.MainActivity
import `in`.mahato.tambola.db.AppDatabase
import `in`.mahato.tambola.game.model.TambolaRule
import `in`.mahato.tambola.rule.entity.SavedRuleEntity
import `in`.mahato.tambola.rule.entity.WinningPrizeEntity
import `in`.mahato.tambola.ui.theme.BlackText
import `in`.mahato.tambola.ui.theme.ShamockGreen
import `in`.mahato.tambola.ui.theme.WhiteBg
import `in`.mahato.tambola.ui.theme.WhiteText
import kotlinx.coroutines.launch

class SummaryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using proper type-safe intent retrieval for newer Android versions if needed,
        // but keeping your standard implementation for compatibility.
        val rules = intent.getParcelableArrayListExtra<TambolaRule>("rules") ?: emptyList()
        val totalPoints = intent.getIntExtra("totalPoints", 0)

        enableEdgeToEdge()
        setContent {
            SummaryScreen(
                rules = rules,
                total = totalPoints,
                onConfirmSave = { finalEntities ->
                    saveToDatabase(finalEntities)
                }
            )
        }
    }

    private fun saveToDatabase(entities: List<SavedRuleEntity>) {
        lifecycleScope.launch {
            try {

                val db = AppDatabase.getDatabase(this@SummaryActivity)

                val ruleDao = db.ruleDao()
                val prizeDao = db.winningPrizeDao()


                prizeDao.clearAll()

                val prizeEntities = entities.flatMap { savedRule ->
                    List(savedRule.quantity) {
                        WinningPrizeEntity(
                            savedRule = savedRule,
                            isClaimed = false
                        )
                    }
                }

                // 4️⃣ Save prizes
                prizeDao.insertPrizes(prizeEntities)
                // replaceRules handles the Delete + Insert transaction
                ruleDao.replaceRules(entities)

                Toast.makeText(this@SummaryActivity, "Distribution Saved Successfully", Toast.LENGTH_SHORT).show()
                // Redirect to MainActivity
                val intent = Intent(this@SummaryActivity, MainActivity::class.java).apply {
                    // Clear the backstack so the user can't return to the Summary screen
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish() // Close SummaryActivity

            } catch (e: Exception) {
                Toast.makeText(this@SummaryActivity, "Error saving: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun SummaryScreen(
    rules: List<TambolaRule>,
    total: Int,
    onConfirmSave: (List<SavedRuleEntity>) -> Unit
) {
    // track saving state to disable button and show progress if needed
    var isSaving by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(PurpleBgStart, PurpleBgEnd)))

            .padding(16.dp)
    ) {
        Column {
            Text(
                "FINAL DISTRIBUTION SUMMARY",
                fontSize = 22.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
            )

            // Grand Total Display
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("GRAND TOTAL", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    Text("$total PTS", fontSize = 26.sp,  color = GoldButton)
                }
            }

            // Read-Only List
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f))
            ) {
                Column(Modifier.padding(16.dp)) {
                    SummaryHeaderRow()
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.15f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    LazyColumn(Modifier.fillMaxSize()) {
                        items(rules) { rule ->
                            // Calculate total amount for this rule
                            val amount = ((rule.percentage / 100.0) * total * rule.quantity).toInt()
                            SummaryItemRow(rule, amount)
                            HorizontalDivider(color = Color.White.copy(alpha = 0.05f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            var saveDistributionFocused by remember { mutableStateOf(false) }
            Button(
                enabled = !isSaving,
                onClick = {
                    isSaving = true
                    val entities = rules.map { rule ->
                        val amountPerItem = ((rule.percentage / 100.0) * total).toInt()
                        SavedRuleEntity(
                            ruleId = rule.id,
                            ruleName = rule.name,
                            percentage = rule.percentage,
                            quantity = rule.quantity,
                            amountPerItem = amountPerItem,
                            totalRuleAmount = amountPerItem * rule.quantity
                        )
                    }
                    onConfirmSave(entities)
                },
                modifier = Modifier.fillMaxWidth().height(60.dp) .onFocusChanged { saveDistributionFocused = it.isFocused },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (saveDistributionFocused)
                        WhiteBg
                    else ShamockGreen,
                    contentColor = if (saveDistributionFocused)
                        BlackText
                    else WhiteText
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.Black)
                } else {
                    Text("SAVE & DONE", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun SummaryHeaderRow() {
    Row(Modifier.fillMaxWidth()) {
        Text("RULE", Modifier.weight(2f), color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text("QTY", Modifier.weight(0.8f), color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Text("AMOUNT", Modifier.weight(1.2f), color = Color.LightGray, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.End)
    }
}

@Composable
fun SummaryItemRow(rule: TambolaRule, amount: Int) {
    var focused by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .background(
                if (focused) Color.White.copy(alpha = 0.15f) else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .onFocusChanged { focused = it.isFocused }
            .focusable(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(2f)) {
            Text(rule.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("${rule.percentage}% of total", color = Color.White.copy(0.5f), fontSize = 11.sp)
        }
        Text(
            "${rule.quantity}",
            Modifier.weight(0.8f),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
        Text(
            "$amount",
            Modifier.weight(1.2f),
            color = GoldButton,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.End,
            fontSize = 18.sp
        )
    }
}
