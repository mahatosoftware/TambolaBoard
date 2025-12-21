package `in`.mahato.tambola.rule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import `in`.mahato.tambola.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
class RuleSelectionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(color = Color.Transparent) {
                    TambolaRuleSelectionScreen()
                }
            }
        }
    }
}


