package ir.javid.sattar.todolist.core.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.javid.sattar.todolist.core.local.TodoDatabase
import ir.javid.sattar.todolist.core.local.dao.TodoDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase = Room.databaseBuilder(
        context,
        TodoDatabase::class.java,
        "todo-database"
    ).build()

    @Provides
    @Singleton
    fun provideDao(
        db: TodoDatabase
    ): TodoDao = db.todoDao()
}
