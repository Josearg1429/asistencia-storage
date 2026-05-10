plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.elcarmen.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.elcarmen.app"
        minSdk = 26                  // Android 8.0 — requerido por EncryptedSharedPreferences
        targetSdk = 34
        versionCode = 5
        versionName = "5.0"
    }

    signingConfigs {
        create("release") {
            storeFile = rootProject.file("elcarmen-release.jks")
            storePassword = "ElCarmen2025!"
            keyAlias = "elcarmen"
            keyPassword = "ElCarmen2025!"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    // Copia los archivos web al directorio assets antes de compilar
    applicationVariants.all {
        val variantName = name.replaceFirstChar { it.uppercase() }
        tasks.register<Copy>("copyWebAssets$variantName") {
            from(rootProject.projectDir.parentFile) {
                include("index.html", "styles.css", "script.js")
                include("libs/**")
            }
            into(layout.projectDirectory.dir("src/main/assets"))
        }
        preBuildProvider.configure {
            dependsOn("copyWebAssets$variantName")
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    // Cifrado AES-256-GCM para reemplazar localStorage
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
}
