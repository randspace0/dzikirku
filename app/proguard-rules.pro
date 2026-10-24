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

# Gson reflects on field names and generic types
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses
-keep class * extends com.google.gson.reflect.TypeToken
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# JSON, Firestore and Room-converted models are matched by field name at runtime
-keepclassmembers class id.gritlabs.dzikirku.model.** { *; }
-keepclassmembers class id.gritlabs.dzikirku.data.** { *; }
