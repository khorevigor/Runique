import java.util.Properties

plugins {
    alias(libs.plugins.runique.android.application.compose)
    alias(libs.plugins.google.services.plugin)
}

android {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProperties["KEYSTORE_PATH"] as String)
            storePassword = localProperties["STORE_PASSWORD"] as String
            keyPassword = localProperties["KEY_PASSWORD"] as String
            keyAlias = localProperties["KEY_ALIAS"] as String
        }
    }
    namespace = "com.dsphoenix.runique"

    defaultConfig {

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    dynamicFeatures += setOf(":analytics:analytics_feature")
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {

    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.notification)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(projects.auth.domain)
    implementation(projects.auth.data)
    implementation(projects.auth.presentation)

    implementation(projects.run.data)
    implementation(projects.run.location)
    implementation(projects.run.domain)
    implementation(projects.run.presentation)
    implementation(projects.run.network)

    implementation(projects.core.connectivity.domain)
    implementation(projects.core.connectivity.data)

    // Coil
    implementation(libs.coil.compose)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    // Koin
    implementation(libs.bundles.koin)

    api(libs.feature.delivery)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Location
    implementation(libs.google.android.gms.play.services.location)

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Timber
    implementation(libs.timber)

    // Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.appcheck.debug)
}
