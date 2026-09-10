# -----------------------------------------------------------------------------
# R8 / ProGuard Optimization & Obfuscation Rules
# -----------------------------------------------------------------------------

# Enable aggressive R8 optimizations & access modification
-optimizationpasses 5
-allowaccessmodification
-repackageclasses ''

# Preserve line numbers and source files for clean crash traces (Play Console / Crashlytics)
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,InnerClasses,EnclosingMethod

# Renaming source file attribute for cleaner obfuscation output
-renamesourcefileattribute SourceFile

# -----------------------------------------------------------------------------
# App Specific Rules
# -----------------------------------------------------------------------------

# Keep Room DB entities, DAOs and migrations
-keepclassmembers class * extends androidx.room.RoomDatabase {
    <init>();
}
-keep @androidx.room.Entity class * { *; }
-dontwarn androidx.room.paging.**

# Keep Data Models / Entities if serialized or passed via Parcelable
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Firebase & Play Services Ads optimization
-dontwarn com.google.android.gms.**
-dontwarn com.google.firebase.**

# Keep Google Mobile Ads SDK and AdConfig for release builds
-keep class com.google.android.gms.ads.** { *; }
-keep class com.google.ads.** { *; }
-keep class in.mahato.tambola.util.AdConfig { *; }