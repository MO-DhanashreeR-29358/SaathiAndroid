#~~~~~~~~~~~ DISTA PROGAURD STARTS ~~~~~~~~~~~~~
-keep class dataRepository.** {*;}
-keep class intentFactory.** {*;}
-keep class views.** {*;}
-keep class ma.poi.models.** {*;}
-keep class ma.poi.controllers.** {*;}
-keep class ma.poi.enums.** {*;}
-keep class processors.coreProcessors.** {*;}
-keep class ma.dista.activities.reports.models.** {*;}
-keep class ma.dista.activities.poimapsview.providers.** {*;}
-keep class processors.coreProcessors.ActionType {*;}
#
-keep class dataRepository.preferences.encryptedPreferences.EncryptedSharedPref {*;}
-keep class com.google.crypto.** { *; }
-keep class com.google.firebase.firestore.** { *; }
#~~~~~~~~~~~ Custom Form Listener
-keep public class * implements ma.poi.inject.IFormListener {
# public static final ** getInstance();
public static <methods>;
}
#~~~~~~~~~~~ Couchbase Repository Classes
-keep class ma.dista.repository.**{*;}
#~~~~~~~~~~~ Dynamic Form Listener Model Classes
-keep class ma.dista.activities.poi.template.models.** {*;}
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
#~~~~~~~~~~~ DISTA PROGAURD ENDS ~~~~~~~~~~~~~
-dontwarn java.lang.invoke.**
-keep class java.lang.reflect.**
#~~~~~~~~~~~ BASIC PROGAURD SETTING STARTS ~~~~~~~~~~~~
#Use 5 step of optimization
-optimizationpasses 5
#When not preverifing in a case-insensitive filing system, such as Windows.
#This tool will unpack your processed jars,(if using windows you should then
#use):
-dontusemixedcaseclassnames
#Specifies not to ignore non-public library classes. As of version 4.5, this
#is the default setting
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
-optimizations !code/simplification/arithmetic,!field