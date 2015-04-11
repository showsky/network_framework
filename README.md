Casa Library
---
Casa lib is common utils collection, convenient for Thread / Log / Error Handler

Dependence Third Party Library
---
Build-in Gradle File

* Volley
* OkHttp
* Android Support V7 Appcompat
* Gson

Set-Up
---

* Gradle File

Setting Uniform compileSdkVersion and buildToolsVersion

``` gradle
File: project build.gradle pattern
---

apply plugin: 'com.android.application'

android {
    compileSdkVersion rootProject.ext.compileSdkVersion
    buildToolsVersion rootProject.ext.buildToolsVersion

    packagingOptions {
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
    }

    defaultConfig {
        applicationId "com.miiicasa.bj_wifi_truck"
        minSdkVersion 16
        targetSdkVersion 21
        versionCode 35
        versionName "3.e"
    }

    signingConfigs {
        debug {
            storeFile file("../debug.keystore")
        }

        release {
            storeFile file("../production.keystore");
            storePassword "******"
            keyAlias "******"
            keyPassword "******"
        }
    }

    productFlavors {
        beta {
            buildConfigField "String", "API_DOMAIN", "\"api.ihuihe.cn\""
        }

        prod {
            buildConfigField "String", "API_DOMAIN", "\"api.ihuihe.cn\""
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }

        debug {
            signingConfig signingConfigs.debug
            minifyEnabled false
            shrinkResources false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'

        }
    }

    applicationVariants.all { variant ->
        variant.outputs.each { output ->
            def outputFile = output.outputFile
            if (outputFile != null && outputFile.name.endsWith('.apk')) {
                def fileName = outputFile.name.replace(".apk", "-${defaultConfig.versionName}-${defaultConfig.versionCode}.apk")
                output.outputFile = new File(outputFile.parent, fileName)
            }
        }
    }
}
```

* Config

```java
File: Config.java
---

public class Config {

    public final static int STORAGE_CACHE_SIZE = 1024 * 1024 * 30;
    public final static int MEMORY_CACHE_SIZE = 30;

    public final static String NETWORK_DEFAULT_USER_AGENT = "miiicasa";
    public final static int NETWORK_CONNECT_TIMEOUT_SECOND = 20;
    public final static boolean USE_PERSISTENT_COOKIE = true;
    public final static boolean USE_SSL = false;

    public static final boolean PROXY = false;
    public static final String PROXY_IP = "192.168.0.100";
    public static final int PROXY_PORT = 8888;

    public static final boolean RETRY_NETWORK = true;
    public static final int RETRY_MAX = 2;

    public final static int THREAD_POOL_SIZE = 3;
    public final static int THREAD_POOL_MAX_SZIE = 5;
    public final static int THREAD_KEEP_ALIVE_TIME = 60;
}
```


```gradle
File: root project is build.gradle

...

ext {
    compileSdkVersion = 21
    buildToolsVersion = "21.1.2"
}

...
```

* ProGuard rules

```
# okhttp
-dontwarn com.squareup.**
-keep class com.squareup.** { *; }

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
```

* Logger

```java
File: Config.java
---

public final static String PROJECT_NAME = "casa";
public final static boolean DEBUG = true;

```

```java
File: App.java
---

class App extends Appliction {

	public App() {
		Logger.setProject(Config.PROJECT, Config.DEBUG);
	}

}

```

Module
---

* Logger
* Network
* Exception
	* NetworkExcpetion
	* ApiExcpetion
* Run
* Utils

Test Case
---

* NetworkTest
* RunTest

Author
---

name: Ting Cheng

email: showsky@gmail.com

Change log
---