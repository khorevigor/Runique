plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.dsphoenix.auth.presentation"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(libs.androidx.navigation.runtime.ktx)
}
