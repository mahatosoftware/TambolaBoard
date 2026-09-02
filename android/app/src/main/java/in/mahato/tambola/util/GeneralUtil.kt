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

    /**
     * Opens the Google Play Store page for the Tambola Tickets app.
     */
    fun openPlayStore(context: android.content.Context, packageName: String = "in.mahato.tambolaticket") {
        try {
            val intent = android.content.Intent(
                android.content.Intent.ACTION_VIEW,
                android.net.Uri.parse("market://details?id=$packageName")
            ).apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            val webIntent = android.content.Intent(
                android.content.Intent.ACTION_VIEW,
                android.net.Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            ).apply {
                addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(webIntent)
        }
    }
}