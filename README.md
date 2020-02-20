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
        implementation 'com.github.Teknasyon-Teknoloji:desk360-android-sdk:0.6.10'
}
```



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
import com.teknasyon.desk360.helper.Desk360Config
```

```
##### fun Desk360Constants.startDesk360(context: Context,
					token: String,
					targetId: String,
					appKey: String,
					appVersion: String,
					baseURL: String,
					deviceToken: String):Intent

| Parameters   | Description                                                  |
| ------------ | ------------------------------------------------------------ |
| token        | your firebase token |
| targetId     | ticket id from firebase message body  
| appKey       | desk360 Api Key
| appVersion   | your application's build number
| baseURL      | desk360 Url
| deviceToken  | your device id

##### Add below activiy to your AndroidManifest.xml file into application tag.

```
<application
	...
	<activity
     android:name="com.teknasyon.desk360.view.activity.Desk360BaseActivity"
     android:windowSoftInputMode="stateHidden|adjustResize"/>
</application>
```

```
### GetFirebase Token
```

FirebaseInstanceId.getInstance().instanceId

                .addOnCompleteListener { task ->
		
                    if (task.isSuccessful && task.result != null) {
                        MyApplication.instance.fireBaseToken = task.result!!.token
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

            val intent = Desk360Constants.initDesk360(
                    context = this,
                    token = "your firebase token",
                    targetId = "targetId from notification body",
                    appKey = "desk360 api key",
                    appVersion = "app version",
                    baseURL = "desk360 url",
                    deviceToken = "your device id")
		    
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        } ?: run {
            PendingIntent.getActivity(this, 0, Intent(this, YourStartingActivity::class.java), PendingIntent.FLAG_ONE_SHOT)
        }

```
### Use Desk 360
```

 val intent = Desk360Constants.initDesk360(
                    context = this,
                    token = "your firebase token",
                    targetId = "targetId from notification body",
                    appKey = "desk360 api key",
                    appVersion = "app version",
                    baseURL = "desk360 url",
                    deviceToken = "your device id")

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP )
                    startActivity(intent)
                    finish()		

```
### Open Desk360 without Notification Service
```
If your app will not use notification then you must give token "" and for targetId ""

val intent = Desk360Constants.initDesk360(
                    context = this,
                    token = "your firebase token",
                    targetId = "targetId from notification body",
                    appKey = "desk360 api key",
                    appVersion = "app version",
                    baseURL = "desk360 url",
                    deviceToken = "your device id")

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP )
                    startActivity(intent)
                    finish()		
		    
# Versioning

We use [SemVer](http://semver.org/) for versioning.



# Support

If you have any questions or feature requests, please create an issue.


# Licence

Copyright Teknasyon 2019.

Desk360 is released under the MIT license. See [LICENSE](https://github.com/Teknasyon-Teknoloji/desk360-android-sdk/blob/master/LICENSE)  for more information.
