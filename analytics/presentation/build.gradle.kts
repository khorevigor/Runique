plugins {
    alias(libs.plugins.runique.android.feature.ui)
}

android {
    namespace = "com.dsphoenix.analytics.presentation"
}

dependencies {
    implementation(projects.analytics.domain)
}
