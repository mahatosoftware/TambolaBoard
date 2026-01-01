
package `in`.mahato.tambola

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import `in`.mahato.tambola.game.GameActivity
import `in`.mahato.tambola.rule.RuleSelectionActivity
import `in`.mahato.tambola.ui.theme.AppTheme
import `in`.mahato.tambola.winner.ViewWinnersActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

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

        onSelectGameRule={
            val intent = Intent(context, RuleSelectionActivity::class.java)

            context.startActivity(intent)
            if (context is MainActivity) context.finish()
        },

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
        onViewWinners = {
            val intent = Intent(context, ViewWinnersActivity::class.java)
            context.startActivity(intent)
        },
        onExit = {
            if (context is MainActivity) context.finish()
        }
    )
}

