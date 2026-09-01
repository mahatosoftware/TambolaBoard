package `in`.mahato.tambola

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.android.gms.ads.MobileAds
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

class TambolaApplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(`in`.mahato.tambola.util.LanguageUtil.wrapContext(base))
    }

    override fun onCreate() {
        super.onCreate()
        val lang = `in`.mahato.tambola.util.LanguageUtil.getSelectedLanguage(this)
        `in`.mahato.tambola.util.LanguageUtil.applyLanguage(lang)

        MobileAds.initialize(this) {}
        FirebaseApp.initializeApp(this)
        
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            if (BuildConfig.DEBUG) {
                DebugAppCheckProviderFactory.getInstance()
            } else {
                PlayIntegrityAppCheckProviderFactory.getInstance()
            }
        )
    }
}
