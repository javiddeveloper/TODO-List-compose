plugins {
    alias(libs.plugins.todolist.android.library)
    alias(libs.plugins.todolist.android.hilt)
    alias(libs.plugins.room)
}

android {
    namespace = "ir.javid.sattar.todolist.core.local"

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core:domain"))
    
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)
}
