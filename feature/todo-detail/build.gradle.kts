plugins {
    alias(libs.plugins.todolist.android.feature)
}

android {
    namespace = "ir.javid.sattar.todolist.feature.todo_detail"
}

dependencies {
    implementation(project(":core:data"))
}
