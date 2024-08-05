plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.dsphoenix.core.notification"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.koin)
}
