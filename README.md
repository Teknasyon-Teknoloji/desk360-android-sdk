# Desk360 -android-sdk

 ![img](https://img.shields.io/badge/kotlin-v1.3.11-brightgreen.svg?logoColor=orange&logo=kotlin)   [![img](https://camo.githubusercontent.com/383e4033d81b12128804ca3208b4ebdd6e00e5f0/68747470733a2f2f6a69747061636b2e696f2f762f6b6f6265756d75742f557064617465436865636b65722e737667)](https://jitpack.io/#kobeumut/UpdateChecker)  ![img](https://img.shields.io/badge/Sdk-14+-brightgreen.svg?logoColor=orange)



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
        implementation 'com.github.Teknasyon-Teknoloji:desk360-android-sdk:0.1.9'
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



##### Start Desk360 with app_key -and an optinal device_token-.

> Note: If no device_token is provided , Desk360 will generate random token which might cause your app to lose tickets when the app is deleted.



```
import com.teknasyon.desk360.helper.Desk360Config
```



 ```
Desk360Config().context = yourContext

Desk360Constants.desk360CurrentTheme("light")

Desk360Constants.desk360Config(
            BuildConfig.APP_KEY,
            "your_device_token",
            BuildConfig.VERSION_NAME,
            BuildConfig.DESK360_BASE_URL
        ) 	
```	



##### fun desk360Config(app_key: String, device_token: String?= null)

| Parameters   | Description                                                  |
| ------------ | ------------------------------------------------------------ |
| app_key      | Uniqe token request from [http://desk360.com]                |
| device_token | This parameters  values has a optionally ,  If you do not specify any value, the Desk360 sdksi generates a random deviceId.<br/>  *DeviceId created by Desk360; Resets when user uninstall and reinstall application* |

####  

##### Add below activiy to your AndroidManifest.xml file into application tag.

```
<application
	...
	<activity
     android:name="com.teknasyon.desk360.view.activity.Desk360BaseActivity"
     android:windowSoftInputMode="stateHidden|adjustResize"/>
</application>
```



#### Customize Desk360 theme:

Desk360Constants.currentTheme = "dark"

or

Desk360Constants.currentTheme = "light"



### Use Desk 360

```
 startActivity(Intent(context, Desk360BaseActivity::class.java))
```



# Versioning

We use [SemVer](http://semver.org/) for versioning.



# Support

If you have any questions or feature requests, please create an issue.



# Licence

Copyright Teknasyon 2019.

Desk360 is released under the MIT license. See [LICENSE](https://github.com/Teknasyon-Teknoloji/desk360-android-sdk/blob/master/LICENSE)  for more information.
