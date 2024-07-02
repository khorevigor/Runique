
import com.android.build.api.dsl.DynamicFeatureExtension
import com.dsphoenix.convention.ExtensionType
import com.dsphoenix.convention.addUiLayerDependencies
import com.dsphoenix.convention.configureAndroidCompose
import com.dsphoenix.convention.configureBuildTypes
import com.dsphoenix.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidDynamicFeatureConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.dynamic-feature")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<DynamicFeatureExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)

                configureBuildTypes(commonExtension = this, extensionType = ExtensionType.DYNAMIC_FEATURE)
            }

            dependencies {
                addUiLayerDependencies(target)
            }
        }
    }
}
