# android-xml-run-listener

An [AndroidJUnitRunner](https://developer.android.com/reference/android/support/test/runner/AndroidJUnitRunner.html) [RunListener](http://junit.org/junit4/javadoc/latest/org/junit/runner/notification/RunListener.html) implementation which will create JUnit compatible XML report files containing the results of Andorid Instrumentation tests.

[![Download](https://api.bintray.com/packages/schroepf/schroepf/android-xml-run-listener/images/download.svg) ](https://bintray.com/schroepf/schroepf/android-xml-run-listener/_latestVersion)
[![Build Status](https://travis-ci.org/schroepf/TestLab.svg?branch=master)](https://travis-ci.org/schroepf/TestLab)

## Integrating android-xml-run-listener

Add dependency to `build.gradle`:

```
androidTestCompile 'de.schroepf:android-xml-run-listener:0.3.0'
```

## Activating the XmlRunListener

Add the following to your app's `build.gradle` file's `defaultConfig` section:
```
android {
    defaultConfig {

        //...

        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArgument "listener", "de.schroepf.androidxmlrunlistener.XmlRunListener"
    }
```

## [Alternative] Use from command line

When you don't want to add the XmlRunListener to your project's gradle config you may add the following option:
```
-e listener de.schroepf.androidxmlrunlistener.XmlRunListener
```
to the `adb shell am instrument` command, e.g.:
```
adb shell am instrument -w -r -e listener de.schroepf.androidxmlrunlistener.XmlRunListener  -e debug false de.schroepf.demoapp.test/android.support.test.runner.AndroidJUnitRunner
```

## Retrieve report xml file

The `report.xml` files will be stored in the tests application's documents folder, e.g.:
```
adb shell cat /sdcard/Android/data/de.schroepf.demoapp/files/report*.xml
```

to copy them from the device to the computer:
```
adb pull /sdcard/Android/data/de.schroepf.demoapp/files
```

## IMPORTANT: Remove reports from the device

Bofore starting the next test run make sure to remove the report XML files from the device either by
uninstalling the app before running the test again or by executing:

```
adb shell rm /sdcard/Android/data/de.schroepf.demoapp/files/report*.xml
```
