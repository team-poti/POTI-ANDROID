import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
}

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

fun requiredLocalProperty(key: String): String =
    requireNotNull(properties[key] as? String) {
        "$key is required in local.properties"
    }

android {
    namespace = "com.poti.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.poti.android"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val kakaoNativeAppKey = properties["kakao.native.app.key"].toString()
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "USE_UI_MOCK", "false")
        }
        create("mock") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".mock"
            versionNameSuffix = "-mock"
            buildConfigField("boolean", "USE_UI_MOCK", "true")
            matchingFallbacks += listOf("debug")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "USE_UI_MOCK", "false")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "server"
    productFlavors {
        create("dev") {
            dimension = "server"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "BASE_URL", requiredLocalProperty("poti.dev.base.url"))
        }
        create("prod") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", requiredLocalProperty("poti.prod.base.url"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

ktlint {
    android = true
    coloredOutput = true
    verbose = true
    outputToConsole = true
}

dependencies {
    // --- Android Core & Lifecycle ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // --- UI (Jetpack Compose) ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)

    // --- Dependency Injection (Hilt) ---
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.lottie.compose)
    ksp(libs.hilt.android.compiler)

    // --- Network (Retrofit & OkHttp) ---
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.json)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.kotlinx.serialization)

    // --- Image Loading (Coil) ---
    implementation(libs.coil.compose)

    // --- Local Storage ---
    implementation(libs.androidx.datastore.preferences)

    // --- Utils ---
    implementation(libs.timber)
    implementation(libs.immutable)

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // --- Debugging ---
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    "mockImplementation"(libs.androidx.ui.tooling)
    "mockImplementation"(libs.androidx.ui.test.manifest)

    // Kakao
    implementation(libs.kakao.user)
}
