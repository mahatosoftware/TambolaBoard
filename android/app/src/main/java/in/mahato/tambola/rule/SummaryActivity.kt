package `in`.mahato.tambola.rule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SummaryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the rules and points passed via Intent
        val rules = intent.getParcelableArrayListExtra<TambolaRule>("rules") ?: emptyList<TambolaRule>()
        val totalPoints = intent.getIntExtra("totalPoints", 0)
        val allocatedPoints = intent.getIntExtra("allocatedPoints", 0)

        setContent {
            SummaryScreen(rules, allocatedPoints, totalPoints)
        }
    }
}

@Composable
fun SummaryScreen(
    rules: List<TambolaRule>,
    allocated: Int,
    total: Int
) {
    val remaining = total - allocated
    val color = if (remaining == 0) MintButton else Color.Red

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(PurpleBgStart, PurpleBgEnd))
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Text(
                "SUMMARY",
                fontSize = 22.sp,
                color = Color.White
            )

            Spacer(Modifier.height(16.dp))

            /* -------- USED / REMAINING -------- */
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Used", color = Color.LightGray, fontSize = 12.sp)
                    Text("$allocated", color = Color.White, fontSize = 18.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Remaining", color = Color.LightGray, fontSize = 12.sp)
                    Text("$remaining", color = color, fontSize = 18.sp)
                }
            }

            Spacer(Modifier.height(16.dp))

            /* -------- LIST OF RULES -------- */
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(rules) { rule ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            rule.name,
                            modifier = Modifier.weight(2f),
                            color = Color.White
                        )
                        Text(
                            rule.quantity.toString(),
                            modifier = Modifier.weight(1f),
                            color = GoldButton
                        )
                        Text(
                            "${rule.percentage}%",
                            modifier = Modifier.weight(1f),
                            color = Color.White
                        )
                    }
                }
            }

            /* -------- AD SPACE -------- */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Advertisement",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}
