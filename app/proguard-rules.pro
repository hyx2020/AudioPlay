# copy from https://blog.csdn.net/weixin_37716758/article/details/87877193
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Tools\AndroidSDK/tools/proguard/proguard-android.txt
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

####################基本配置#####################

#指定压缩级别(在0~7之间，默认为5，一般不需要更改）
-optimizationpasses 5

#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#指定不去忽略非公共的库的类 (默认跳过，有些情况下编写的代码与类库中的类在同一个包下，并且持有包内容的引用，此时就需要提示不跳过）
-dontskipnonpubliclibraryclasses

#混合时采用的算法(一般不改变）
-optimizations !code/simplification/arithmetic,!filed\/*,!class/merging \/*

#混淆时不使用大小写混合，混淆后的类名为小写(大小写混淆容易导致class文件相互覆盖）
-dontusemixedcaseclassnames

#不做预检验，preverify是proguard的四大步骤之一,可以加快混淆速度
-dontpreverify

#混淆后生成映射文件
-verbose
-printmapping proguardMapping.txt

#保护代码中的Annotation不被混淆(这在Json实体映射是非常重要，例如FastJson)
-keepattributes *Annotation*

#避免混淆泛型，
#这在JSON实体映射时非常重要，比如fastJson
-keepattribute Signature

#保留本地方法不被混淆
-keepclasseswithmembernames class * {
		native <method>;
		}



#设置抛出异常时保留代码行号
-keepattributes SourceFile.LineNumberTable

#忽略警告 (慎用）
-ignorewarnings



#输出apk包内所有的class的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从apk中删除的代码
-printusage unused.txt
=


#======================项目配置=======================
#保留所有的本地native方法不被混淆
-keepclasseswithmembernames class *{
    native <methods>;
 }
 #保留继承自Activity、Application这些类的子类
 -keep public class * extends android.app.Activity
 -keep public class * extends android.app.Application
 -keep public class * extends android.app.Service
 -keep public class * extends android.app.BroadcastReceiver
 -keep public class * extends android.app.ContentProvider
 -keep public class * extends android.app.backup.BackupAgentHelper
 -keep public class * extends android.preference.Preference
 -keep public class * extends android.view.View
 -keep public class com.android.vending.licensing.ILicensingService

#如果有引用android-support-v4.jar包，可以添加下面这行
-keep public class xxx(包名).fragment.**{*;}

#保留在Activity中的方法参数是view的方法
#从而我们在layout里面编写onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
	public void *(android.view.View);
	}

 -keep public class * extends android.database.sqlite.SQLiteOpenHelper{*;}
 -keepnames class * extends android.view.View
 -keep class * extends android.app.Fragment {
     public void setUserVisibleHint(boolean);
     public void onHiddenChanged(boolean);
     public void onResume();
     public void onPause();
 }
 -keep class android.support.v4.app.Fragment {
     public void setUserVisibleHint(boolean);
     public void onHiddenChanged(boolean);
     public void onResume();
     public void onPause();
 }
 -keep class * extends android.support.v4.app.Fragment {
     public void setUserVisibleHint(boolean);
     public void onHiddenChanged(boolean);
     public void onResume();
     public void onPause();
 }


 #如果引用v4或者v7包
 -dontwarn android.support.**
 #以防onClick不被影响，保留Activity中包含view的方法
 -keepclasseswithmembers class * extends android.app.Activity{
    public void * (android.view.View);
 }
 #枚举类不能被混淆
 -keepclassmembers enum *{
    public static **[] values();
    public static ** valueOf(java.lang.String);
    }

 #保留自定义控件不能被混淆(即继承自View)不能被混淆
 -keep public class * extends android.view.View{
    public <int>(android.content.Context);
    public <int>(android.content.Context,android.util.AttributeSet);
    public <int>(android.content.Context,android.util.AttributeSet,int);
    public void set*(***);
    *** get*();
    }

  #保留Parcelable序列化的类不能被混淆
  -keep class * implements android.os.Parcelable{
    public static final android.os.Parcelable$Creator *;
    }

  #保留Serializable序列化的类不能被混淆
  -keepclassmembers class * implements java.io.Serializable{
    static final long serialVersionUID;
    private static final java.io.ObjectStreamFiled[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
   #对R文件下的所有类及其方法都不能混淆
   -keep class class **.R$*{
        *;
   }

   #对于回调函数onXXEvent的，不能被混淆
   -keepclassmembers class * {
   void *(**Event);
   }





   #第三方框架的混淆
  #okhttp
  -keep class com.squareup.okhttp.** { *;}
  -dontwarn okio.**
  -keepclassmembers class **.R$* {
      public static <fields>;
  }
  #eventbus
  -keepattributes *Annotation*
  -keepclassmembers class ** {
      @org.greenrobot.eventbus.Subscribe <methods>;
  }
  -keep enum org.greenrobot.eventbus.ThreadMode { *; }
  # Only required if you use AsyncExecutor
  -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
      <init>(java.lang.Throwable);
  }
  #retroift
  -dontwarn retrofit2.**
  -keep class retrofit2.** { *; }
  -keepattributes Signature
  -keepattributes Exceptions

  #ButterKnife
  -keep class butterknife.** { *; }
  -dontwarn butterknife.internal.**
  -keep class **$$ViewBinder { *; }

  -keepclasseswithmembernames class * {
      @butterknife.* <fields>;
  }

  -keepclasseswithmembernames class * {
      @butterknife.* <methods>;
  }

  #fastjson
  -dontwarn com.alibaba.fastjson.**
  -keep class com.alibaba.fastjson.** { *; }

  #rxjava
  -dontwarn sun.misc.**
  -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
  }
  -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode producerNode;
  }
  -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
   rx.internal.util.atomic.LinkedQueueNode consumerNode;
  }
#Facebook
-keep class com.facebook.** {*;}
-keep interface com.facebook.** {*;}
-keep enum com.facebook.** {*;}

#Fresco
-keep class com.facebook.fresco.** {*;}
-keep interface com.facebook.fresco.** {*;}
-keep enum com.facebook.fresco.** {*;}
  #友盟分享
  -dontwarn com.google.android.maps.**
  -dontwarn android.webkit.WebView
  -dontwarn com.umeng.**
  -dontwarn com.tencent.weibo.sdk.**
  -dontwarn com.facebook.**
  -keep public class javax.**
  -keep public class android.webkit.**
  -dontwarn android.support.v4.**
  -keep class android.support.** {*;}
  -keep enum com.facebook.**
  -keepattributes Exceptions,InnerClasses,Signature
  -keepattributes *Annotation*
  -keepattributes SourceFile,LineNumberTable
  -keep public interface com.facebook.**
  -keep public interface com.tencent.**
  -keep public interface com.umeng.socialize.**
  -keep public interface com.umeng.socialize.sensor.**
  -keep public interface com.umeng.scrshot.**

    #对引入的webview不能进行混
    -keepclassmembers class 自己webview包下{
    public *;
    }
    -keepclassmembers class * extends android.webkit.webViewClient{
    public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
    public boolean *(android.webkit.WebView,java.lang.String);
   }
   -keepclassmembers class * extends android.webkit.webViewClient {
   public void *(android.webkit.webView,java.lang.String);
   }

	#对JavaScript的处理 需要将js使用到的原生方法不被混淆
	-keepclassmembers class xxx.xxx.xxx.xxActivity$Jsxx{
	<method>;
	}

    #包含反射的处理
    -keep class 自己的反射类的包.** {*;}

################################### -项目自定义- #######################################
   #保留实体类和成员不被混淆
   -keep public class 自己实体包名.bean.**{
	public void set*(***);
	public  *** get*();
	public *** is*();
  }

   #保留内部类 $是区别内嵌类与其母体的标志
   -keep class com.xxx.xxx.xxxActivity$*{ *; }