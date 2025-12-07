package `in`.mahato.tambola.util


object GeneralUtil {

    /**
     * Returns a copyright message automatically using the current year.
     * Example: "© 2025 Tambola. All rights reserved."
     */
    fun getCopyrightMessage(
        appName: String = "Tambola Board",
        ownerName: String? = "Debasish Mahato"   // optional if same as app name
    ): String {
        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val nameToShow = ownerName ?: appName

        return "© $year $nameToShow."
    }
}