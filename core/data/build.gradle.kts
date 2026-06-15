plugins {
    alias(libs.plugins.todolist.android.library)
    alias(libs.plugins.todolist.android.hilt)
}

android {
    namespace = "ir.javid.sattar.todolist.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:local"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    
    implementation(libs.androidx.paging.runtime)
}
