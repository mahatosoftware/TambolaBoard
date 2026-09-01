
package `in`.mahato.tambola

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import `in`.mahato.tambola.game.GameActivity
import `in`.mahato.tambola.rule.RuleSelectionActivity
import `in`.mahato.tambola.ui.theme.AppTheme
import `in`.mahato.tambola.winner.ViewWinnersActivity
import `in`.mahato.tambola.gamemode.GameModeSelectionActivity
import `in`.mahato.tambola.util.AdInterstitialHelper
import android.app.Activity

class MainActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: android.content.Context) {
        super.attachBaseContext(`in`.mahato.tambola.util.LanguageUtil.wrapContext(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val currentLang = `in`.mahato.tambola.util.LanguageUtil.getSelectedLanguage(this)
        `in`.mahato.tambola.util.LanguageUtil.applyLanguage(currentLang)
        AdInterstitialHelper.loadInterstitialAd(this)

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
            // No-op or removed, as button will be removed
        },

        onNewGame = {
            if (context is Activity) {
                AdInterstitialHelper.showInterstitialAd(context) {
                    val intent = Intent(context, GameModeSelectionActivity::class.java)
                    context.startActivity(intent)
                }
            } else {
                val intent = Intent(context, GameModeSelectionActivity::class.java)
                context.startActivity(intent)
            }
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

