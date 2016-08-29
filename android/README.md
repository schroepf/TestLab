# android-xml-run-listener

## Integrating android-xml-run-listener

Add dependency to `build.gradle`:
```
androidTestCompile 'de.schroepf:android-xml-run-listener:0.1.3'
```

## Use from Android Studio

Add the following option to "Extra options" of your Instrumentation Tests run configurations:
```
-e listener de.schroepf.androidxmlrunlistener.XmlRunListener
```

## Use from command line

Add following option:
```
-e listener de.schroepf.androidxmlrunlistener.XmlRunListener
```
to the `adb shell am instrument` command, e.g.:
```
adb shell am instrument -w -r -e listener de.schroepf.androidxmlrunlistener.XmlRunListener  -e debug false de.schroepf.demoapp.test/android.support.test.runner.AndroidJUnitRunner
```

## Retrieve report xml file

The `report.xml` file will be stored in the tests application's documents folder, e.g.:
```
adb shell cat /storage/emulated/0/Android/data/de.schroepf.demoapp/files/report.xml
```

to copy it from the device to the computer:
```
adb pull /storage/emulated/0/Android/data/de.schroepf.demoapp/files/report.xml
```
