plugins {
    alias(libs.plugins.todolist.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "ir.javid.sattar.todolist.core.network"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
