package `in`.mahato.tambola.gamemode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import `in`.mahato.tambola.ui.theme.AppTheme

class GameIdInputActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                GameIdInputScreen(
                    onFinish = {
                        finish()
                    }
                )
            }
        }
    }
}
