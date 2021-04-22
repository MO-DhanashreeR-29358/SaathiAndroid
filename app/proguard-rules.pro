# Add project specific ProGuard rules here.

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**


-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends com.actionbarsherlock.app.SherlockListFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends BaseActivity
-keep public class ContextWrapper
# For native methods, see https://protect-eu.mimecast.com/s/rADECvZvDhygB3ktQtrXG?domain=proguard.sourceforge.net
-keepclasseswithmembernames class * {
 native <methods>;
}
-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}
-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}
-keepclassmembers class * extends android.support.v7.app.AppCompatActivity {
 public void *(android.view.View);
}
# For enumeration classes, see https://protect-eu.mimecast.com/s/4LinCwrwZcRQOWwIqux6t?domain=proguard.sourceforge.net
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
 public static <fields>;
}
-keep class android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class com.actionbarsherlock.** { *; }
-keep interface com.actionbarsherlock.** { *; }
-keep class retrofit2.** { *; }
-dontwarn android.support.**
-dontwarn com.google.ads.**
-dontwarn retrofit2.**
-dontwarn java.nio.file.*
-dontwarn okio.**
-dontwarn nu.xom.**
-dontwarn org.bouncycastle.**
-dontwarn com.fasterxml.**
-dontwarn org.springframework.**
-dontwarn com.amitshekhar.**
# Volley
-dontwarn com.android.volley.**
-dontwarn com.android.volley.error.**
-keep class com.android.volley.** { *; }
-keepattributes Exceptions
-keep interface com.android.volley.** { *; }
-keep class org.apache.commons.logging.*
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
#-optimizations code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!method/inlining/*
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-keep public class BaseActivity
-keep public class BiometricActivityPieSupport
-keep public class BTDiscoveryActivity
-keep public class Decompress
-keep public class DefaultRetryPolicy
-keep public class DemoActivity
-keep public class DownloadFileAsync
-keep public class EsignNsdlActivity
-keep public interface EkycRequestResponseCallBack
-keep public class EsignNsdlActivity
-keep public class HttpsTrustManager
-keep public class IrisActivation
-keep public class IrisAuthenticationActivity
-keep public interface MyResponseCallBack
-keep public class OTPActivity
-keep public interface ResponseCallBack
-keep public class SecBiometricLicenseManager
-keep public class NsdlDeviceDetectActivity
-keep public class WithoutOTGSupportBiometricActivity
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** e(...);
}
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.springframework.**
-dontwarn java.awt.**
-dontwarn javax.security.**
-dontwarn java.beans.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**
-dontwarn com.octo.android.robospice.persistence.**
-dontwarn com.octo.android.robospice.SpiceService
-dontwarn org.apache.commons.**
-keep class org.apache.commons.** { *; }
-dontwarn org.json.**
-keep class org.json.** { *; }
-dontwarn java.nio.**
-keep class java.nio.** { *; }
-keep class com.sec.biometric.** { *; }
-dontwarn com.sec.biometric.**
-dontwarn android.content.**
-keep class android.content.** { *; }