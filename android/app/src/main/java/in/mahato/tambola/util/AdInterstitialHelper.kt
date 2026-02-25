package `in`.mahato.tambola.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.app.UiModeManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdInterstitialHelper {
    private var interstitialAd: InterstitialAd? = null
    private var isAdLoading = false

    fun loadInterstitialAd(context: Context) {
        if (!isPhoneOrTablet(context)) return
        if (interstitialAd != null || isAdLoading) return

        isAdLoading = true
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, AdConfig.INTERSTITIAL_AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
                isAdLoading = false
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
                isAdLoading = false
            }
        })
    }

    fun showInterstitialAd(activity: Activity, onAdDismissed: () -> Unit) {
        if (!isPhoneOrTablet(activity)) {
            onAdDismissed()
            return
        }

        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd(activity) // Preload next
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                    interstitialAd = null
                    onAdDismissed()
                }
            }
            interstitialAd?.show(activity)
        } else {
            // If ad not loaded yet, just proceed
            onAdDismissed()
            loadInterstitialAd(activity)
        }
    }

    fun isPhoneOrTablet(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        return uiModeManager?.currentModeType != Configuration.UI_MODE_TYPE_TELEVISION
    }
}
