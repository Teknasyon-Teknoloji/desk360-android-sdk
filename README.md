# Desk360 -android-sdk

 ![img](https://img.shields.io/badge/kotlin-v1.3.50-brightgreen.svg?logoColor=orange&logo=kotlin)   [![](https://jitpack.io/v/Teknasyon-Teknoloji/desk360-android-sdk.svg)](https://jitpack.io/#Teknasyon-Teknoloji/desk360-android-sdk) ![img](https://img.shields.io/badge/Sdk-14+-brightgreen.svg?logoColor=orange)



# Table Of Content			

- [Project Title](#Summary)
- [Features](#Features)
- [Installation](#Installation)
- [Usage](#Usage)
- [Versioning](#Versioning) 
- [Licence](#Licence)

## Summary

Desk360 is an Android SDK to help your embedding customer support in your mobile Android apps with ease.

## Features

The Desk360 SDK lets users do any of the following:

Create new support tickets
View and comment on existing tickets
Interactively communicate with related support teams



# Installation



### Setup

To integrate Desk360 into your Android project , add below parts to your  build.gradlle

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency

```
dependencies {
        implementation 'com.github.Teknasyon-Teknoloji:desk360-android-sdk:latest_release'
}
```

(Please change latest_release with : https://img.shields.io/jitpack/v/github/Teknasyon-Teknoloji/desk360-android-sdk)


Or Maven

**Step 1.** Add the JitPack repository to your build file

```markup
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

**Step 2.** Add the dependency



```markup
	<dependency>
	    <groupId>com.github.Teknasyon-Teknoloji</groupId>
	    <artifactId>desk360-android-sdk</artifactId>
	    <version>Tag</version>
	</dependency>
```



# Usage


##### Start Desk360 #####

```
import com.teknasyon.desk360.helper.Desk360Constants

```

```
##### val desk360SDKManager = Desk360SDKManager.Builder()
            .appKey(key:String)
            .appVersion(version:String)
            .languageCode(code:String)
            .environment(environment:String)
            .platform(platform:Platform)
            .countryCode(code:String)
            .theme(themeId:Int)
            .jsonObject(json:JSONObject?)
            .build()

        val desk360Client = desk360SDKManager.initialize(_targetId = "", _token =  "", _deviceToken = "")
        desk360Client.start(this)

| Parameters   | Description                                                  |
| ------------ | ------------------------------------------------------------ |
| token        | your firebase token |
| targetId     | ticket id from firebase message body  
| deviceToken  | your Android device id
| appKey       | desk360 Api Key will provided when you get the license
| appVersion   | your application's version number
| languageCode | ISO 639-1 Code	for sdk language: "en","fr,"tr
| environment  | flag for sandbox or production api, Environment object class should be used
| platform     | mobile platform: Platform.GOOGLE or Platform.HUAWEI
| countryCode  | country code: "tr", "us", "de"
| theme        | custom theme id
| jsonObject   | for custom datas


```
### Add to your AndroidManifest.xml
```

<application
	...
	<activity
     android:name="com.teknasyon.desk360.view.activity.Desk360BaseActivity"
     android:windowSoftInputMode="stateHidden|adjustResize"
     android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>

</application>

```
### Network Security Config
```

Create network security config xml and add it to your application

<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">teknasyon.desk360.com</domain>
    </domain-config>
</network-security-config>

<application
	...
    android:networkSecurityConfig="@xml/network_security_config"

 </application>


or you can just add this line to your application

<application
	...
    android:usesCleartextTraffic="true"

 </application>


```
### GetFirebase Token
```

FirebaseInstanceId.getInstance().instanceId

                .addOnCompleteListener { task ->
		
                    if (task.isSuccessful && task.result != null) {
                       val token = task.result!!.token
                    }
                }	

```
### Parse "targetId" from Firebase Notification Body (Starting Activity)
```

When your application is killed the notification body will be in your starting activity's extra.

val bundle = intent.extras
        bundle?.let {
	
	val hermes = bundle?.getString("hermes")
            hermes?.let {
		val targetId = Desk360Constants.getTicketId(hermes)
    	}	
  }

```
### Parse "targetId" from Firebase Notification Body (Firebase Notification Service)
```

When your application is on foreground onMessageReceived will handle notification body.

override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val hermes = remoteMessage.data["hermes"]

        hermes?.let {
	val targetId = Desk360Constants.getTicketId(hermes)
    }
    
```
### Handling "targetId"
```

"if target_id is not null you must open Desk360SplasActivity if not you must open your starting activity"

Example (In your firebaseMessagingService class) :

 val pendingIntent: PendingIntent?

        pendingIntent = targetId?.let { targetId ->

            val desk360SDKManager = Desk360SDKManager.Builder()
            .appKey("app key")
            .appVersion("app version")
            .languageCode("your selected ISO 639-1 Code for language: tr, en")
            .environment("environment info: Environment.PRODUCTION or Environment.SANDBOX" )
            .platform("mobile platform: Platform.GOOGLE or Platform.HUAWEI")
            .countryCode("country code: tr, de")
            .theme("theme id")
            .jsonObject("for custom data")
            .addIntentFlags(arrayOf(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_SINGLE_TOP))
            .build()

        val desk360Client = desk360SDKManager.initialize(
            _targetId = "targetId from notification body",
            _token = "your firebase token",
            _deviceToken = "your Android device id"
        )

        val intent = desk360Client.getIntent(this)

            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        } ?: run {
            PendingIntent.getActivity(this, 0, Intent(this, YourStartingActivity::class.java), PendingIntent.FLAG_ONE_SHOT)
        }

```
### Use Desk 360
```

        val desk360SDKManager = Desk360SDKManager.Builder()
            .appKey("app key")
            .appVersion("app version")
            .languageCode("your selected ISO 639-1 Code for language: tr, en")
            .environment("environment info: Environment.PRODUCTION or Environment.SANDBOX" )
            .platform("mobile platform: Platform.GOOGLE or Platform.HUAWEI")
            .countryCode("country code: tr, de")
            .theme("theme id")
            .jsonObject("for custom data")
            .addIntentFlags(arrayOf(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_SINGLE_TOP))
            .build()

        val desk360Client = desk360SDKManager.initialize(
            _targetId = "targetId from notification body",
            _token = "your firebase token",
            _deviceToken = "your Android device id"
        )

        desk360Client.start(this)
        finish()		

```
### Open Desk360 without Notification Service
```
If your app will not use notification then you must set token "" and for targetId ""

        val desk360SDKManager = Desk360SDKManager.Builder()
            .appKey("app key")
            .appVersion("app version")
            .languageCode("your selected ISO 639-1 Code for language: tr, en")
            .environment("environment info: Environment.PRODUCTION or Environment.SANDBOX" )
            .platform("mobile platform: Platform.GOOGLE or Platform.HUAWEI")
            .countryCode("country code: tr, de")
            .theme("theme id")
            .jsonObject("for custom data")
            .addIntentFlags(arrayOf(Intent.FLAG_ACTIVITY_CLEAR_TOP, Intent.FLAG_ACTIVITY_SINGLE_TOP))
            .build()

        val desk360Client = desk360SDKManager.initialize(
            _targetId = "targetId from notification body",
            _token = "your firebase token",
            _deviceToken = "your Android device id"
        )

        desk360Client.start(this)
        finish()
```
### Language
```

If you don't want to use custom language then you must set to "" , desk360 sdk will use your Android device language

```
# ProGuard
```

If you are using proguard you must add this rules to avoid further compile issues.

-keep class com.teknasyon.desk360.model.** { *; }
-keepnames class com.teknasyon.desk360.model.** { *; }
-keep class com.teknasyon.desk360.modelv2.** { *; }
-keepnames class com.teknasyon.desk360.modelv2.** { *; }

```


# Versioning

We use [SemVer](http://semver.org/) for versioning.


# Support

If you have any questions or feature requests, please create an issue.


# Licence

Copyright Teknasyon 2019.

Desk360 is released under the MIT license. See [LICENSE](https://github.com/Teknasyon-Teknoloji/desk360-android-sdk/blob/master/LICENSE)  for more information.
