# android-test-rules

This library offers some test rules which make Android Instrumentation tests more convenient.

[ ![Download](https://api.bintray.com/packages/schroepf/schroepf/android-test-rules/images/download.svg) ](https://bintray.com/schroepf/schroepf/android-test-rules/_latestVersion)
[![Build Status](https://travis-ci.org/schroepf/TestLab.svg?branch=master)](https://travis-ci.org/schroepf/TestLab)

## Gradle Setup

Add the dependency to the `dependencies` section of your app's `build.gradle` file of:
```
dependencies {
  ...

  androidTestImplementation 'de.schroepf:android-test-rules:0.0.1'
  ...
}
```

## `NoAnimationsRule`

To disable animations before running tests and enable them again when tests have finished:

```
@ClassRule
public static NoAnimationsRule noAnimationsRule = new NoAnimationsRule();
```

Compatible: Android api level >= 24 (Android N or newer)

## `DemoModeRule`

Enable android's built-in [demo mode](https://android.googlesource.com/platform/frameworks/base/+/master/packages/SystemUI/docs/demo_mode.md)
to cleanup the ui before taking screenshots:

```
    @ClassRule
    public static DemoModeRule demoModeRule = new DemoModeRule.Builder()
            .clock("1201")
            .batteryLevel(80)
            .batteryPlugged(false)
            .notifications(false)
            .bluetooth(BluetoothState.CONNECTED)
            .build();
```

Compatible: TBD

## `ScreenshotActivityRule`

Take a screenshot right before the instrumented Activity will be finished (i.e. right before the
test case will finish):


### Setup

Instead of using `ActivityTestRule` use `ScreenshotActivityRule` to launch the instrumented activity:
```
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ScreenshotActivityRule<>(MainActivity.class);
```

### Usage:

Prefix any test case whose result should be captured into a screenshot with a `@Screenshot` annotation.

```
    @Screenshot
    @Test
    public void screenshotTest() { }
```

Parameters:
- `name`: name of the screenshot file (defaults to the name of the test method)
- `subdirectory`: name of the subdirectory und which the screenshot will be saved (defaults to the name of the test class)
- `format`: bitmap format used to save the screenshot (defaults to [Bitmap.CompressFormat.PNG](https://developer.android.com/reference/android/graphics/Bitmap.CompressFormat))

The screenshots will be saved in the app's directory and can be retrieved via ADB:
```
adb pull /sdcard/Android/data/de.schroepf.demoapp/files
```
(replace `de.schroepf.demoapp` with the package name of your app)
