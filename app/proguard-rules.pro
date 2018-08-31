# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\android-sdks/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-okio.pro
# Okio (Updated-04/11/2016)
-dontwarn okio.**

# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-okhttp3.pro
# OkHttp (Updated-04/11/2016)
-dontwarn okhttp3.**

-dontwarn javax.annotation.**

-keep public class com.mosl.mobile.biometric.ui.BioMetricMainActivity
-keep public class com.mosl.mobile.ekyc.common.MOSLCommon
-keep class org.json.**{ *; }
-dontwarn org.json.**

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-dontwarn okio.**

-dontwarn androidx.**
-keep class androidx.** { *; }