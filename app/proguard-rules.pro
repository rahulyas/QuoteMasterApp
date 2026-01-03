# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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

############################################
## 🚫 Suppress Unused Warnings (Safe) ##
############################################
-dontwarn com.google.auto.service.AutoService

# Generated automatically by Android Gradle plugin - suppress missing class warnings
-dontwarn android.app.ActivityThread$ActivityClientRecord
-dontwarn android.app.ActivityThread
-dontwarn android.app.ApplicationPackageManager
-dontwarn android.app.LoadedApk
-dontwarn android.app.QueuedWork
-dontwarn android.app.ResourcesManager
-dontwarn android.app.admin.DevicePolicyState
-dontwarn android.app.admin.EnforcingAdmin
-dontwarn android.app.admin.PolicyState
-dontwarn android.app.admin.StringPolicyValue
-dontwarn android.app.usage.BroadcastResponseStats
-dontwarn android.bluetooth.IBluetoothManagerCallback
-dontwarn android.bluetooth.le.IAdvertisingSetCallback
-dontwarn android.companion.virtual.VirtualDeviceManager$VirtualDevice
-dontwarn android.compat.Compatibility
-dontwarn android.content.pm.PackageParser$Package
-dontwarn android.content.pm.PackageParser
-dontwarn android.content.pm.SuspendDialogInfo
-dontwarn android.content.res.ApkAssets
-dontwarn android.content.res.ResourcesImpl
-dontwarn android.content.res.XmlBlock$Parser
-dontwarn android.database.sqlite.SQLiteConnection
-dontwarn android.graphics.BaseCanvas
-dontwarn android.graphics.animation.RenderNodeAnimator
-dontwarn android.hardware.camera2.impl.CameraDeviceImpl
-dontwarn android.hardware.display.DisplayManagerGlobal
-dontwarn android.hardware.input.InputManagerGlobal
-dontwarn android.hardware.input.VirtualKeyboard
-dontwarn android.hardware.input.VirtualMouse
-dontwarn android.hardware.input.VirtualTouchscreen
-dontwarn android.hardware.location.ContextHubClient
-dontwarn android.hardware.location.ContextHubTransaction$Response
-dontwarn android.hardware.location.ContextHubTransaction
-dontwarn android.hardware.location.NanoAppInstanceInfo
-dontwarn android.net.wifi.WifiScanner
-dontwarn android.provider.Settings$Config
-dontwarn android.service.voice.AlwaysOnHotwordDetector$EventPayload
-dontwarn android.service.voice.AlwaysOnHotwordDetector
-dontwarn android.telecom.InCallAdapter
-dontwarn android.telecom.Phone
-dontwarn android.util.Log$TerribleFailure
-dontwarn android.util.Log$TerribleFailureHandler
-dontwarn android.view.DisplayEventReceiver
-dontwarn android.view.InputEventReceiver
-dontwarn android.view.InsetsController
-dontwarn android.view.RenderNodeAnimator
-dontwarn android.view.ViewRootImpl
-dontwarn android.view.WindowManagerGlobal
-dontwarn android.view.WindowManagerImpl
-dontwarn android.window.BackMotionEvent
-dontwarn com.android.internal.app.AlertController
-dontwarn com.android.internal.content.om.OverlayConfig
-dontwarn com.android.internal.os.BackgroundThread
-dontwarn com.android.internal.util.AnnotationValidations
-dontwarn dalvik.system.CloseGuard
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn libcore.icu.LocaleData
-dontwarn libcore.util.NativeAllocationRegistry

############################################
## 📦 Retrofit + Kotlin + Coroutine Rules + suspend functions ##
############################################

# Retrofit core - CRITICAL: Keep all Retrofit classes and methods
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Keep all service interfaces and their methods
-keep interface * {
    @retrofit2.http.* <methods>;
}

# CRITICAL: Keep generic signatures and annotations for Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

# Keep Retrofit service method return types (fixes ParameterizedType casting)
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Keep OkHttp classes
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep class okio.** { *; }

# Keep Gson/JSON serialization classes
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

############################################
## 🔧 Kotlin Coroutines & Suspend Functions ##
############################################

# Keep all coroutine-related classes
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Keep Kotlin metadata (CRITICAL for suspend functions)
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.Continuation
-keep class kotlin.coroutines.jvm.internal.BaseContinuationImpl
-keep class kotlin.jvm.internal.** { *; }

############################################
## 🧠 Models and API Services (Reflection) ##
############################################

# Keep ALL your API service interfaces and model classes
-keep class com.quotemaster.quotemasterapp.data.remote.** { *; }
-keep interface com.quotemaster.quotemasterapp.data.remote.** { *; }

# Keep all data model classes with all their fields
-keep class com.quotemaster.quotemasterapp.data.model.** { *; }
-keep class com.quotemaster.quotemasterapp.domain.model.** { *; }

# Specifically keep your API classes (redundant but safe)
-keep class com.quotemaster.quotemasterapp.data.model.PixabayResponse { *; }
-keep class com.quotemaster.quotemasterapp.domain.model.PixabayImage { *; }
-keep class com.quotemaster.quotemasterapp.data.model.OpenAIResponse { *; }
-keep class com.quotemaster.quotemasterapp.data.model.OpenAIRequest { *; }
-keep class com.quotemaster.quotemasterapp.data.remote.PixabayApi { *; }
-keep class com.quotemaster.quotemasterapp.data.remote.OpenAIService { *; }

############################################
## 🧩 Compose Runtime ##
############################################

# Compose internals
-keep class androidx.compose.runtime.** { *; }
-keepclassmembers class ** {
    @androidx.compose.runtime.Composable *;
}
-keep class androidx.compose.runtime.snapshots.** { *; }

############################################
## ✅ Additional Protection ##
############################################

# Keep all annotations
-keepattributes *Annotation*

# Prevent removal of reflectively accessed fields
-keepclassmembers class ** {
    *** value_;
}

# Keep enum classes
-keepclassmembers enum * { *; }

# General reflection keep rules
-keep class java.lang.reflect.** { *; }

############################################
## 🎯 Specific Fixes for Your Error ##
############################################

# CRITICAL: Prevent ClassCastException in Retrofit's HttpServiceMethod
-keep class retrofit2.HttpServiceMethod { *; }
-keep class retrofit2.ServiceMethod { *; }
-keep class retrofit2.RequestFactory { *; }
-keep class retrofit2.ParameterHandler { *; }
-keep class retrofit2.Response { *; }
-keep class retrofit2.Call { *; }

# Keep generic type information for API responses
-keepattributes Exceptions