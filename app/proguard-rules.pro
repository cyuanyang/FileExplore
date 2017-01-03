# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/study/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# 混淆时是否做预校验
-dontpreverify
-dontoptimize
# 是否使用大小写混合
-dontusemixedcaseclassnames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-repackageclasses ''
-allowaccessmodification

# 指定代码的压缩级别
-optimizationpasses 7
# 混淆时是否记录日志
-verbose
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn
-dontwarn net.poemcode.*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class org.apache.avalon.framework.logger.Logger
-keep class android.support.v4.**{*;}
-keep class android.support.v7.**{*;}
-keep public class org.apache.log4j.Category
-keep public class org.apache.log4j.Logger
-keep public class org.apache.log4j.Priority
-keep public class org.apache.log.Hierarchy
-keep public class javax.servlet.ServletContextListener
-keep public class javax.servlet.ServletContextEvent

-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class **.R$*

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

 # 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}


-keep public class * extends android.view.View{*;}
-keep public class * implements java.io.Serializable{*;}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}