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
    alias(libs.plugins.google.services)
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

fun requiredLocalProperty(key: String): String {
    val value = localProperties[key] as? String

    require(!value.isNullOrBlank()) {
        "$key must not be blank in local.properties"
    }

    return value
}

fun buildConfigString(value: String): String = "\"$value\""

fun environmentVariableOrLocalProperty(
    environmentVariable: String,
    localProperty: String,
): String? =
    System.getenv(environmentVariable)?.takeIf(String::isNotBlank)
        ?: localProperties.getProperty(localProperty)?.takeIf(String::isNotBlank)

val uploadStoreFile = environmentVariableOrLocalProperty("UPLOAD_STORE_FILE", "upload.store.file")
val uploadStorePassword = environmentVariableOrLocalProperty("UPLOAD_STORE_PASSWORD", "upload.store.password")
val uploadKeyAlias = environmentVariableOrLocalProperty("UPLOAD_KEY_ALIAS", "upload.key.alias")
val uploadKeyPassword = environmentVariableOrLocalProperty("UPLOAD_KEY_PASSWORD", "upload.key.password")
val uploadSigningValues = listOf(uploadStoreFile, uploadStorePassword, uploadKeyAlias, uploadKeyPassword)
val isUploadSigningConfigured = uploadSigningValues.all { !it.isNullOrBlank() }

require(uploadSigningValues.none { !it.isNullOrBlank() } || isUploadSigningConfigured) {
    "Upload signing requires UPLOAD_STORE_FILE, UPLOAD_STORE_PASSWORD, " +
        "UPLOAD_KEY_ALIAS, and UPLOAD_KEY_PASSWORD. " +
        "Set all four as environment variables or upload.* values in local.properties."
}

val appVersionCode =
    providers.gradleProperty("VERSION_CODE")
        .orElse(providers.environmentVariable("VERSION_CODE"))
        .orElse("1")
        .get()
        .let { value ->
            val parsedValue = value.toIntOrNull()
            require(parsedValue != null && parsedValue in 1..2_100_000_000) {
                "VERSION_CODE must be an integer between 1 and 2100000000, but was '$value'."
            }
            parsedValue
        }

val appVersionName =
    providers.gradleProperty("VERSION_NAME")
        .orElse(providers.environmentVariable("VERSION_NAME"))
        .orElse("1.0.0")
        .get()
        .also { value ->
            require(value.isNotBlank()) {
                "VERSION_NAME must not be blank."
            }
        }

android {
    namespace = "com.poti.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.poti.android"
        minSdk = 28
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val kakaoNativeAppKey = localProperties["kakao.native.app.key"].toString()
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = kakaoNativeAppKey
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }

        if (isUploadSigningConfigured) {
            create("release") {
                storeFile = rootProject.file(requireNotNull(uploadStoreFile))
                storePassword = requireNotNull(uploadStorePassword)
                keyAlias = requireNotNull(uploadKeyAlias)
                keyPassword = requireNotNull(uploadKeyPassword)
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
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
            signingConfig = signingConfigs.findByName("release")
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
            buildConfigField("String", "BASE_URL", buildConfigString(requiredLocalProperty("poti.dev.base.url")))
            buildConfigField("String", "DEEP_LINK_HOST", buildConfigString("https://dev-app.poti.kr"))
            buildConfigField("String", "HTTP_LOG_LEVEL", buildConfigString("BODY"))
            manifestPlaceholders["deepLinkHost"] = "dev-app.poti.kr"
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                buildConfigString(requiredLocalProperty("google.dev.web.client.id")),
            )
        }
        create("prod") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", buildConfigString(requiredLocalProperty("poti.prod.base.url")))
            buildConfigField("String", "DEEP_LINK_HOST", buildConfigString("https://app.poti.kr"))
            buildConfigField("String", "HTTP_LOG_LEVEL", buildConfigString("BASIC"))
            manifestPlaceholders["deepLinkHost"] = "app.poti.kr"
            buildConfigField(
                "String",
                "GOOGLE_WEB_CLIENT_ID",
                buildConfigString(requiredLocalProperty("google.prod.web.client.id")),
            )
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
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)

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
    testImplementation(libs.mockito.core)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // --- Debugging ---
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    "mockImplementation"(libs.androidx.ui.tooling)
    "mockImplementation"(libs.androidx.ui.test.manifest)

    // Social
    implementation(libs.kakao.user)
    implementation(libs.googleid)
    implementation(libs.kakao.share)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
}
