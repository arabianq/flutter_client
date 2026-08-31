import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

configurations.all {
    val tinkAndroid = "com.google.crypto.tink:tink-android:1.21.0"
    resolutionStrategy {
        force(tinkAndroid)
        dependencySubstitution {
            substitute(module("com.google.crypto.tink:tink")).using(module(tinkAndroid))
        }
    }
}

val requestedTasks = gradle.startParameter.taskNames.joinToString(" ").lowercase()
if (requestedTasks.contains("fcm")) {
    apply(plugin = "com.google.gms.google-services")
}

// Branding overrides parsed from the shared dart-define file, so the app can
// be renamed (and installed in parallel with the original client) from a
// single place. Falls back to the upstream defaults when absent.
val customDefinesFile = rootProject.file("../tool/dart_defines/custom.json")
val customDefines: Map<String, String> = if (customDefinesFile.exists()) {
    Regex("\"([A-Za-z0-9_]+)\"\\s*:\\s*\"([^\"]*)\"")
        .findAll(customDefinesFile.readText())
        .associate { it.groupValues[1] to it.groupValues[2] }
} else {
    emptyMap()
}

fun customDefine(key: String, fallback: String): String = customDefines[key] ?: fallback

val appDisplayName = customDefine("APP_NAME", "Fluxer")
val applicationIdBase = customDefine("APP_ID", "com.fluxer")

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("key.properties")
val hasKeystoreProperties = keystorePropertiesFile.exists()
if (hasKeystoreProperties) {
    keystorePropertiesFile.inputStream().use { inputStream ->
        keystoreProperties.load(inputStream)
    }
}

android {
    namespace = "com.fluxer"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion
    flavorDimensions += listOf("environment", "push")

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "canary-debug"
            keyAlias = "canary"
            keyPassword = "canary-debug"
        }
    }

    defaultConfig {
        applicationId = applicationIdBase
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
        multiDexEnabled = true
        manifestPlaceholders["appLabel"] = appDisplayName
        manifestPlaceholders["buildEnvironment"] = "stable"
        manifestPlaceholders["pushProvider"] = "fcm"
    }

    productFlavors {
        create("canary") {
            dimension = "environment"
            applicationIdSuffix = ".canary"
            versionNameSuffix = "-canary"
            manifestPlaceholders["appLabel"] = "$appDisplayName Canary"
            manifestPlaceholders["buildEnvironment"] = "canary"
        }
        create("stable") {
            dimension = "environment"
            manifestPlaceholders["appLabel"] = appDisplayName
            manifestPlaceholders["buildEnvironment"] = "stable"
        }
        create("beta") {
            dimension = "environment"
            manifestPlaceholders["appLabel"] = "$appDisplayName Beta"
            manifestPlaceholders["buildEnvironment"] = "beta"
        }
        create("fcm") {
            dimension = "push"
            manifestPlaceholders["pushProvider"] = "fcm"
        }
        create("unifiedpush") {
            dimension = "push"
            manifestPlaceholders["pushProvider"] = "unifiedpush"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = if (hasKeystoreProperties) {
                signingConfigs.create("release") {
                    val storeFilePath = keystoreProperties["storeFile"] as String
                    storeFile = file(storeFilePath)
                    storePassword = keystoreProperties["storePassword"] as String
                    keyAlias = keystoreProperties["keyAlias"] as String
                    keyPassword = keystoreProperties["keyPassword"] as String
                }
            } else {
                signingConfigs.getByName("debug")
            }
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

flutter {
    source = "../.."
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.4")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.14.1")
    add("fcmImplementation", platform("com.google.firebase:firebase-bom:33.9.0"))
    add("fcmImplementation", "com.google.firebase:firebase-messaging")
    add("fcmImplementation", "com.google.android.gms:play-services-cloud-messaging")
}
