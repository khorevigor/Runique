package com.dsphoenix.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *>,
    extensionType: ExtensionType,
    minifyEnabled: Boolean = false
) {
    commonExtension.run {
        buildFeatures {
            buildConfig = true
        }

        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(minifyEnabled)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, minifyEnabled)
                        }
                    }
                }
            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(minifyEnabled)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, minifyEnabled)
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType(minifyEnabled)
                        }
                        release {
                            configureReleaseBuildType(commonExtension, minifyEnabled = false)
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(minifyEnabled: Boolean = false) {
    isMinifyEnabled = minifyEnabled
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *>,
    minifyEnabled: Boolean = true
) {
    isMinifyEnabled = minifyEnabled
    proguardFiles(
        "proguard-rules.pro"
    )
}
