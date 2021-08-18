package spiral.bit.dev.sunsetnotesapp.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import spiral.bit.dev.sunsetnotesapp.data.managers.PreferenceManager
import spiral.bit.dev.sunsetnotesapp.data.repositories.NotesRepositoryImpl
import spiral.bit.dev.sunsetnotesapp.data.repositories.TaskRepositoryImpl
import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.repositories.INoteRepository
import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository

val repositoryModule = module {
    single<INoteRepository> { NotesRepositoryImpl(noteDao = get()) }
    single<ITaskRepository> { TaskRepositoryImpl(taskDao = get()) }
    single<IPreferenceManager> { PreferenceManager(context = androidContext()) }
}