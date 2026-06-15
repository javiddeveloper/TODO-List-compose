plugins {
    alias(libs.plugins.todolist.android.feature)
}

android {
    namespace = "ir.javid.sattar.todolist.feature.todo_list"
}

dependencies {
    implementation(project(":core:data"))
    
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.paging.compose)
}
