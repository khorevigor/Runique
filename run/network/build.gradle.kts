plugins {
    alias(libs.plugins.runique.android.library)
}

android {
    namespace = "com.dsphoenix.run.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
}
