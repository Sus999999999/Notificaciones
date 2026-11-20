# Configuración fuera del código Dart (Ajolotito)

Estos pasos tocan archivos que genera `flutter create`, por eso no van en el zip.

## 0. Crear el proyecto y copiar los archivos
```
flutter create ajolotito
cd ajolotito
# copia aquí lib/, test/, pubspec.yaml y analysis_options.yaml de este zip (reemplazando)
# borra el test de ejemplo:
rm test/widget_test.dart
flutter pub get
flutter test
```

## 1. Android: android/app/src/main/AndroidManifest.xml
Dentro de `<manifest>` (fuera de `<application>`):
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
```
Dentro de `<application>`:
```xml
<receiver android:exported="false"
    android:name="com.dexterous.flutterlocalnotifications.ScheduledNotificationReceiver" />
<receiver android:exported="false"
    android:name="com.dexterous.flutterlocalnotifications.ScheduledNotificationBootReceiver">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED"/>
        <action android:name="android.intent.action.MY_PACKAGE_REPLACED"/>
        <action android:name="android.intent.action.QUICKBOOT_POWERON" />
        <action android:name="com.htc.intent.action.QUICKBOOT_POWERON"/>
    </intent-filter>
</receiver>
```

## 2. Android: android/app/build.gradle.kts (o build.gradle)
```kotlin
android {
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}
dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
}
```
Versiones exactas (desugar_jdk_libs, Java, compileSdk): las que indique el README de flutter_local_notifications para la versión instalada.

## 3. iOS: ios/Runner/AppDelegate.swift
En `application(_:didFinishLaunchingWithOptions:)`, antes del `return`:
```swift
UNUserNotificationCenter.current().delegate = self as? UNUserNotificationCenterDelegate
```
Sigue además la sección de iOS del README del paquete (cambios por UIScene).

## 4. Pruebas en teléfono (Ronda 12 del manual)
Usa el "Modo clase" y MINIMIZA la app para recibir los avisos.
En Xiaomi/Huawei/Oppo desactiva el ahorro de batería para la app (dontkillmyapp.com).
