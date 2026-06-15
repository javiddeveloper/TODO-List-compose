import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("todolist.android.library")
                apply("todolist.android.hilt")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.kotlin.plugin.parcelize")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:common"))
                "implementation"(project(":core:domain"))

                "implementation"(libs.findLibrary("androidx.lifecycle.runtime.compose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.compose").get())
                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                
                "implementation"(platform(libs.findLibrary("androidx.compose.bom").get()))
                "implementation"(libs.findLibrary("androidx.ui").get())
                "implementation"(libs.findLibrary("androidx.material3").get())
                "implementation"(libs.findLibrary("androidx.material.icons.extended").get())
                "implementation"(libs.findLibrary("androidx.ui.tooling.preview").get())
                "implementation"(libs.findLibrary("androidx.navigation3.runtime").get())
                "implementation"(libs.findLibrary("androidx.navigation3.ui").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewmodel.navigation3").get())
            }
            
            // Re-enable compose for the module
            val commonExtension = extensions.getByType(com.android.build.api.dsl.CommonExtension::class.java)
            commonExtension.buildFeatures {
                compose = true
            }
        }
    }
}
