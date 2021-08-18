package spiral.bit.dev.sunsetnotesapp.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.DeleteNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.GetNotesUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.InsertNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.UpdateNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.other.implementations.GetPreferenceFlowUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.other.implementations.UpdateSortOrderUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations.*

val useCaseModule = module {

    //notes
    single(named("note_insert")) {
        InsertNoteUseCaseImpl(notesRepository = get())
    }
    single(named("note_update")) {
        UpdateNoteUseCaseImpl(notesRepository = get())
    }
    single(named("note_delete")) {
        DeleteNoteUseCaseImpl(notesRepository = get())
    }
    single(named("note_get")) {
        GetNotesUseCaseImpl(notesRepository = get())
    }

    //tasks
    single(named("task_insert")) {
        InsertTaskUseCaseImpl(taskRepository = get())
    }
    single(named("task_update")) {
        UpdateTaskUseCaseImpl(taskRepository = get())
    }
    single(named("task_delete")) {
        DeleteTaskUseCaseImpl(taskRepository = get())
    }
    single(named("task_delete_all")) {
        DeleteAllCompletedTasksImpl(taskRepository = get())
    }
    single(named("task_get")) {
        GetTasksUseCaseImpl(taskRepository = get())
    }

    //manager
    single(named("manager_sort")) {
        UpdateSortOrderUseCaseImpl(preferenceManager = get())
    }
    single(named("manager_hide")) {
        UpdateHideCompletedUseCaseImpl(preferenceManager = get())
    }
    single(named("manager_get")) {
        GetPreferenceFlowUseCaseImpl(preferenceManager = get())
    }
}