plugins {
    alias(libs.plugins.runique.android.application.wear.compose)
}

android {
    namespace = "com.dsphoenix.wear.app"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {
    implementation(projects.core.presentation.designsystemWear)
    implementation(projects.wear.run.presentation)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.bundles.koin.compose)
}
