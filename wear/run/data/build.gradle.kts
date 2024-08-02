plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.dsphoenix.wear.run.data"

    defaultConfig {
        minSdk = 30
    }
}

dependencies {
    implementation(projects.wear.run.domain)
    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)

    implementation(libs.androidx.health.services.client)
    implementation(libs.bundles.koin)
}
