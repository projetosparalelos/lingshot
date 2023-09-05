-keep class io.grpc.** {*;}
-keepattributes Signature
-keepclassmembers class com.phrase.phrasemaster_domain.model.** {
    *;
 }
-keep class * extends com.google.protobuf.GeneratedMessageLite { *; }
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-dontwarn com.squareup.okhttp.CipherSuite
-dontwarn com.squareup.okhttp.ConnectionSpec
-dontwarn com.squareup.okhttp.TlsVersion
