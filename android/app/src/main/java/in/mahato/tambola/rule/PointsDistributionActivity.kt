package `in`.mahato.tambola.rule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import `in`.ahato.tambola.rule.RuleViewModel
import `in`.mahato.tambola.ui.theme.AppTheme

class PointsDistributionActivity : ComponentActivity() {

    // ✅ Proper ViewModel (lifecycle-aware)
    private val ruleViewModel: RuleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // ✅ 1. Read selected IDs from Intent
        val selectedIds =
            intent.getIntegerArrayListExtra("SELECTED_RULE_IDS") ?: emptyList()

        // ✅ 2. Rebuild selected rules
        val selectedRules =
            tambolaRules.filter { it.id in selectedIds }

        // ✅ 3. Restore state into ViewModel
        ruleViewModel.setRules(selectedRules)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // ✅ Pass ViewModel to screen
                    PointDistributionScreen(ruleViewModel)
                }
            }
        }
    }
}