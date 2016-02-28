# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/kymjs/developer/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-dontoptimize
-keepattributes *Annotation*
-keepattributes Signature

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment{ *; }
-keep class android.support.** { *; } 

##rxjava/okhttp/okio
-dontwarn com.squareup.okhttp.internal.huc.**
-dontwarn okio.**
-dontwarn rx.**
-dontwarn com.squareup.okhttp.internal.http.*
-keepnames class com.levelup.http.okhttp.** { *; }
-keepnames interface com.levelup.http.okhttp.** { *; }
-keepnames class com.squareup.okhttp.** { *; }
-keepnames interface com.squareup.okhttp.** { *; }

##xstream
-libraryjars libs/xstream-1.4.7.jar
-dontwarn com.thoughtworks.xstream.**
-keep class **$$XStreamAlias { *; }
-keep class **$$XStreamImplicit { *; }
-keepnames class * { @com.thoughtworks.xstream.annotations.XStreamImplicit *;}