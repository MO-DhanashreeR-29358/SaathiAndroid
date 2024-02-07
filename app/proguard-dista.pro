#~~~~~~~~~~~        DISTA PROGAURD STARTS       ~~~~~~~~~~~~~
-keep class dataRepository.** {*;}
-keep class intentFactory.** {*;}
-keep class ma.poi.models.** {*;}
-keep class ma.poi.enums.** {*;}
-keep class processors.coreProcessors.** {*;}
-keep class ma.dista.activities.reports.models.** {*;}
-keep class ma.dista.activities.poimapsview.providers.** {*;}
-keep class processors.coreProcessors.ActionType {*;}
#-keep class dataRepository.preferences.encryptedPreferences.EncryptedSharedPref {*;}
-keep class com.google.crypto.** { *; }
-keep class com.google.firebase.firestore.** { *; }

#~~~~~~~~~~~ Custom Form Listener
-keep public class * implements ma.poi.inject.IFormListener {
#   public static final ** getInstance();
   public static <methods>;
}

#~~~~~~~~~~~~ Adapter classes ~~~~~~~~~~~~~~
-keep class ma.dista.jobs.JobsAdapter
-keep class ma.dista.jobs.TwoStepJobAdapter
-keep class ma.dista.jobs.JobActionAdaptor

#~~~~~~~~~~~~ Chatbot ~~~~~~~~~~~~~~
-keep class ai.api.** {*;}
-keepclassmembers class ai.api.** {*;}
-dontwarn ai.api.**
-keep class org.slf4j.** {*;}
-keepclassmembers class org.slf4j.** {*;}
-dontwarn org.slf4j.**
-keep class org.apache.** {*;}
-keepclassmembers class org.apache.** {*;}
-dontwarn org.apache.**class_files
-keep class com.ma.chatbot.** {*;}
-keepclassmembers class com.ma.chatbot.** {*;}
-dontwarn com.ma.chatbot.**

#~~~~~~ PRINTER
#-keep class com.sun.jna.**{*;}

-keep class utils.helpers.SequenceSortUtility {
    public static <methods>;
}
#~~~~~~~~~~~        DISTA PROGAURD ENDS         ~~~~~~~~~~~~~
-dontwarn java.lang.invoke.**
-keep class java.lang.reflect.**

#~~~~~~~~~~~        BASIC PROGAURD SETTING  STARTS     ~~~~~~~~~~~~
#Use 5 step of optimization
-optimizationpasses 5
#When not preverifing in a case-insensitive filing system, such as Windows. This tool will unpack your processed jars,(if using windows you should then use):
-dontusemixedcaseclassnames
#Specifies not to ignore non-public library classes. As of version 4.5, this is the default setting
-dontskipnonpubliclibraryclasses
# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).
#-dontoptimize
#-dontpreverify
-dontwarn class_files.txt
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-dontwarn android.support.**
#Specifies to write out some more information during processing. If the program terminates with an exception, this option will print out the entire stack trace, instead of just the exception message.
-verbose
#To repackage classes on a single package
#-repackageclasses ''
#Keep classes that are referenced on the AndroidManifest
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
#-keep public class com.google.vending.licensing.ILicensingService
#-keep public class com.android.vending.licensing.ILicensingService
#Compatibility library
-keep public class * extends androidx.fragment.app.Fragment
#-keep public class * extends android.app.Fragment
# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
#To remove debug logs:
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
    public static int wtf(...);
}

#Maintain java native methods
-keepclasseswithmembernames class * {
    native <methods>;
}
#To maintain custom components names that are used on layouts XML:
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
 -keepclassmembers public class * extends android.view.View {
  void set*(***);
  *** get*();
}
#Maintain enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#To keep parcelable classes (to serialize - deserialize objects to sent through Intents)
#Keep the R
-keepclassmembers class **.R$* {
    public static <fields>;
}

#Uncomment if using Serializable
-keepclassmembers class * implements java.io.Serializable {
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#~~~~~~~~~~~        BASIC PROGAURD SETTING  ENDS     ~~~~~~~~~~~~

#~~~~~~~~~~~        RETROFIT STARTS     ~~~~~~~~~~~~
# Needed by google-api-client to keep generic types and @Key annotations accessed via ma.pickme.reflection
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}
-keepclasseswithmembers class * {
        @retrofit.http.* <methods>;
}
-keepclasseswithmembers class * {
        @retrofit2.http.* <methods>;
        }
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-keep class retrofit.** { *; }
#
-keep class retrofit2.** { *; }
#Uncomment if using annotations to keep them.
-keepattributes *Annotation*
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by ma.pickme.reflection by converters and base.adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-dontwarn org.xmlpull.v1.**
-dontwarn javax.annotation.**
#~~~~~~~~~~~        RETROFIT ENDS     ~~~~~~~~~~~~

#~~~~~~~~~~~        OKHTTP STARTS     ~~~~~~~~~~~~
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontnote okhttp3.**
#~~~~~~~~~~~        OKHTTP ENDS     ~~~~~~~~~~~~

#~~~~~~~~~~~        OKIO STARTS     ~~~~~~~~~~~~
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#~~~~~~~~~~~        OKIO ENDS     ~~~~~~~~~~~~

#~~~~~~~~~~~        GSON STARTS     ~~~~~~~~~~~~
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
#~~~~~~~~~~~        GSON ENDS     ~~~~~~~~~~~~

#~~~~~~~~~~~        PLAY SERVICE STARTS     ~~~~~~~~~~~~
-dontwarn com.google.android.gms.**
-dontwarn com.google.common.cache.**
-dontwarn com.google.common.primitives.**
#~~~~~~~~~~~        PLAY SERVICE STARTS     ~~~~~~~~~~~~

#~~~~~~~~~~~        RX JAVA STARTS     ~~~~~~~~~~~~
-keep class io.**
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    long producerNode;
    long consumerNode;
}
#~~~~~~~~~~~        RX JAVA ENDS     ~~~~~~~~~~~~

#~~~~~~~~~~~        GLIDE           ~~~~~~~~~~~~~
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#~~~~~~~~~~~ LIFE CYCLER ARCH ~~~~~~~~~~~~~~~~~~
## Android architecture components: Lifecycle
# LifecycleObserver's empty constructor is considered to be unused by proguard
-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}

# ViewModel's empty constructor is considered to be unused by proguard
-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
    <init>(...);
}

# keep Lifecycle State and Event enums values
-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
-keepclassmembers class * {
    @android.arch.lifecycle.OnLifecycleEvent *;
}
-keep class * implements android.arch.lifecycle.LifecycleObserver {
    <init>(...);
}
-keepclassmembers class android.arch.** { *; }
-keep class android.arch.** { *; }
-dontwarn android.arch.**
-keep class * implements android.arch.lifecycle.GeneratedAdapter {<init>(...);}

#~~~~~~~~~~~~ FIREBASE ~~~~~~~~~~~~~~
-keep class com.google.firebase.quickstart.database.viewholder.** {*;}
-keepclassmembers class com.google.firebase.quickstart.database.models.**{*;}

# ProGuard rules for MPAndroidChart
-keep class com.github.mikephil.charting.** { *; }
-dontwarn io.realm.**

-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**

-keep public class * implements ma.dista.utils.DistaApplication {
   public static <methods>;
}





