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


    /**
     * Generates a random alphanumeric Game ID.
     */
    fun generateGameId(): String {
        return (1..5).map { "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".random() }.joinToString("")
    }
}