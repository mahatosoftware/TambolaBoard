package `in`.mahato.tambola.util

import `in`.mahato.tambola.BuildConfig

object AdConfig {
    // Official Google AdMob Test Ad Unit IDs
    private const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

    // Production Ad Unit IDs
    private const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-8382655413286804/2266125437"
    private const val PROD_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-8382655413286804/7364823698"

    val BANNER_AD_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) TEST_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID

    val INTERSTITIAL_AD_UNIT_ID: String
        get() = if (BuildConfig.DEBUG) TEST_INTERSTITIAL_AD_UNIT_ID else PROD_INTERSTITIAL_AD_UNIT_ID
}

