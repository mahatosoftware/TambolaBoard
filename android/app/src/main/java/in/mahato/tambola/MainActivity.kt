
package `in`.mahato.tambola

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import `in`.mahato.tambola.game.GameActivity
import `in`.mahato.tambola.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                MainScreenComposable()
            }
        }
    }
}

@Composable
fun MainScreenComposable() {
    val context = LocalContext.current

    MainScreen(
        onNewGame = {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra("NEW_GAME", true)
            context.startActivity(intent)
            if (context is MainActivity) context.finish()
        },
        onContinue = {
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtra("NEW_GAME", false)
            context.startActivity(intent)
            if (context is MainActivity) context.finish()
        },
        onExit = {
            if (context is MainActivity) context.finish()
        }
    )
}

