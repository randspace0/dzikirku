# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# --- Dzikirku release rules ---

# Optional TLS/markdown providers probed by okhttp and Markwon (from R8 missing_rules.txt)
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**
-dontwarn org.commonmark.ext.gfm.strikethrough.Strikethrough

# kotlinx.serialization: the plugin generates serializers; keep their lookup entry points
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class **$$serializer { *; }
-keepclasseswithmembers class id.gritlabs.dzikirku.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class id.gritlabs.dzikirku.** {
    *** Companion;
}

# Firestore and Room map these models by field name at runtime
-keepclassmembers class id.gritlabs.dzikirku.model.** { *; }
-keepclassmembers class id.gritlabs.dzikirku.data.** { *; }
