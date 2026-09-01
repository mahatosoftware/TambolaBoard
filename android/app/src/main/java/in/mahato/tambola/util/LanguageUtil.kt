package `in`.mahato.tambola.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

data class LanguageOption(
    val code: String,
    val nameEnglish: String,
    val nameNative: String
)

object LanguageUtil {
    private const val PREFS_NAME = "tambola_settings"
    private const val KEY_LANGUAGE = "selected_language"

    val SUPPORTED_LANGUAGES = listOf(
        LanguageOption("en", "English", "English"),
        LanguageOption("hi", "Hindi", "हिंदी"),
        LanguageOption("bn", "Bengali", "বাংলা"),
        LanguageOption("or", "Odia", "ଓଡ଼ିଆ"),
        LanguageOption("kn", "Kannada", "ಕನ್ನಡ"),
        LanguageOption("ta", "Tamil", "தமிழ்"),
        LanguageOption("mr", "Marathi", "मराठी"),
        LanguageOption("te", "Telugu", "తెలుగు"),
        LanguageOption("gu", "Gujarati", "ગુજરાતી"),
        LanguageOption("ml", "Malayalam", "മലയാളം"),
        LanguageOption("pa", "Punjabi", "ਪੰਜਾਬੀ"),
        LanguageOption("as", "Assamese", "অসমীয়া"),
        LanguageOption("es", "Spanish", "Español"),
        LanguageOption("pt", "Portuguese", "Português"),
        LanguageOption("fr", "French", "Français"),
        LanguageOption("de", "German", "Deutsch"),
        LanguageOption("ar", "Arabic", "العربية"),
        LanguageOption("id", "Indonesian", "Bahasa Indonesia"),
        LanguageOption("tr", "Turkish", "Türkçe"),
        LanguageOption("it", "Italian", "Italiano"),
        LanguageOption("ja", "Japanese", "日本語"),
        LanguageOption("ko", "Korean", "한국어"),
        LanguageOption("zh", "Chinese", "中文"),
        LanguageOption("nl", "Dutch", "Nederlands"),
        LanguageOption("ru", "Russian", "Русский"),
        LanguageOption("vi", "Vietnamese", "Tiếng Việt")
    )

    fun getSelectedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun setSelectedLanguage(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()

        applyLanguage(languageCode)

        // Force-update current context resources configuration
        val locale = getTtsLocaleByCode(languageCode)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)

        // Recreate activity so Compose re-evaluates stringResource() with new locale
        val activity = findActivity(context)
        activity?.recreate()
    }

    fun applyLanguage(languageCode: String) {
        val appLocales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocales)
    }

    fun wrapContext(context: Context): Context {
        val languageCode = getSelectedLanguage(context)
        val locale = getTtsLocaleByCode(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun findActivity(context: Context): Activity? {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    fun getTtsLocale(context: Context): Locale {
        return getTtsLocaleByCode(getSelectedLanguage(context))
    }

    private fun getTtsLocaleByCode(code: String): Locale {
        return when (code) {
            "hi" -> Locale("hi", "IN")
            "bn" -> Locale("bn", "IN")
            "or" -> Locale("or", "IN")
            "kn" -> Locale("kn", "IN")
            "ta" -> Locale("ta", "IN")
            "mr" -> Locale("mr", "IN")
            "te" -> Locale("te", "IN")
            "gu" -> Locale("gu", "IN")
            "ml" -> Locale("ml", "IN")
            "pa" -> Locale("pa", "IN")
            "as" -> Locale("as", "IN")
            "es" -> Locale("es", "ES")
            "pt" -> Locale("pt", "PT")
            "fr" -> Locale("fr", "FR")
            "de" -> Locale("de", "DE")
            "ar" -> Locale("ar", "SA")
            "id" -> Locale("id", "ID")
            "tr" -> Locale("tr", "TR")
            "it" -> Locale("it", "IT")
            "ja" -> Locale("ja", "JP")
            "ko" -> Locale("ko", "KR")
            "zh" -> Locale("zh", "CN")
            "nl" -> Locale("nl", "NL")
            "ru" -> Locale("ru", "RU")
            "vi" -> Locale("vi", "VN")
            else -> Locale("en", "US")
        }
    }
}
