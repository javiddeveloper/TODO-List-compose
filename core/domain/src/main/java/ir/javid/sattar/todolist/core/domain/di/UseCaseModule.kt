package ir.javid.sattar.todolist.core.domain.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    // Concrete classes with @Inject constructor don't need explicit provides/binds in a module
    // unless they depend on interfaces or need specific configuration.
}
