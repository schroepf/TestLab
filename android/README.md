# android-xml-run-listener

## Use from Android Studio

Add the following option to "Extra options" of your Instrumentation Tests run configurations:
```
-e listener de.schroepf.androidxmlrunlistener.XmlRunListener
```

## Use from command line

Add

```
-e listener de.schroepf.androidxmlrunlistener.XmlRunListener
```
option to the `adb shell am instrument` command, e.g.:
```
adb shell am instrument -w -r -e listener de.schroepf.androidxmlrunlistener.XmlRunListener  -e debug false de.schroepf.demoapp.test/android.support.test.runner.AndroidJUnitRunner
```
