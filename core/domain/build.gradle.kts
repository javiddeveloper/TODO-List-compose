plugins {
    alias(libs.plugins.todolist.android.library)
    alias(libs.plugins.todolist.android.hilt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "ir.javid.sattar.todolist.core.domain"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.kotlinx.serialization.json)
}
