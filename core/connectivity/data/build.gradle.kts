plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.dsphoenix.core.connectivity.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.connectivity.domain)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(libs.play.services.wearable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.koin)
}
