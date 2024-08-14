plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.jvm.ktor)
}

android {
    namespace = "com.dsphoenix.auth.data"
}

dependencies {
    implementation(projects.auth.domain)
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.auth)

    implementation(libs.timber)

    implementation(libs.bundles.koin)
}
