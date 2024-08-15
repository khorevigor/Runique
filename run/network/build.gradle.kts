plugins {
    alias(libs.plugins.runique.android.library)
    alias(libs.plugins.runique.jvm.ktor)
}

android {
    namespace = "com.dsphoenix.run.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.firestore)
    implementation(libs.google.firebase.storage)
    implementation(libs.timber)

    implementation(libs.bundles.koin)
}
