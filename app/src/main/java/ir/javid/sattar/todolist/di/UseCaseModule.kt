package ir.javid.sattar.todolist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.javid.sattar.todolist.domain.useCases.AddTodoUseCaseImpl
import ir.javid.sattar.todolist.domain.useCases.DeleteTodoUseCaseImpl
import ir.javid.sattar.todolist.domain.useCases.DeleteTodosUseCaseImpl
import ir.javid.sattar.todolist.domain.useCases.GetTodoUseCaseImpl
import ir.javid.sattar.todolist.domain.useCases.PinTodoUseCaseImpl
import ir.javid.sattar.todolist.domain.useCases.TodoListUseCaseImpl
import ir.javid.sattar.todolist.domain.useCases.UpdateTodoUseCaseImpl
import ir.javid.sattar.todolist.domain.useCases.AddTodoUseCase
import ir.javid.sattar.todolist.domain.useCases.DeleteTodoUseCase
import ir.javid.sattar.todolist.domain.useCases.DeleteTodosUseCase
import ir.javid.sattar.todolist.domain.useCases.GetTodoUseCase
import ir.javid.sattar.todolist.domain.useCases.PinTodoUseCase
import ir.javid.sattar.todolist.domain.useCases.TodoListUseCase
import ir.javid.sattar.todolist.domain.useCases.UpdateTodoUseCase

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun provideTodoListUseCase(
        useCase: TodoListUseCaseImpl,
    ): TodoListUseCase

    @Binds
    fun provideAddTodoUseCase(
        useCase: AddTodoUseCaseImpl,
    ): AddTodoUseCase

    @Binds
    fun provideDeleteTodoUseCase(
        useCase: DeleteTodoUseCaseImpl,
    ): DeleteTodoUseCase

    @Binds
    fun provideDeleteTodosUseCase(
        useCase: DeleteTodosUseCaseImpl,
    ): DeleteTodosUseCase

    @Binds
    fun providePinTodoUseCase(
        useCase: PinTodoUseCaseImpl,
    ): PinTodoUseCase

    @Binds
    fun provideUpdateTodoUseCase(
        useCase: UpdateTodoUseCaseImpl,
    ): UpdateTodoUseCase

    @Binds
    fun provideGetTodoUseCase(
        useCase: GetTodoUseCaseImpl,
    ): GetTodoUseCase
}
