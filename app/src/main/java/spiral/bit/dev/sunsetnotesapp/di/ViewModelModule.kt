package spiral.bit.dev.sunsetnotesapp.di

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import spiral.bit.dev.sunsetnotesapp.ui.add_edit.notes.AddEditNoteViewModel
import spiral.bit.dev.sunsetnotesapp.ui.add_edit.tasks.AddEditTaskViewModel
import spiral.bit.dev.sunsetnotesapp.ui.notes.NotesViewModel
import spiral.bit.dev.sunsetnotesapp.ui.tasks.TasksViewModel

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    viewModel { parameters ->
        NotesViewModel(
            state = parameters.get(),
            insertNoteUseCaseImpl = get(named("note_insert")),
            updateSortOrderUseCaseImpl = get(named("manager_sort")),
            deleteNoteUseCaseImpl = get(named("note_delete")),
            getNotesUseCaseImpl = get(named("note_get")),
            getPreferenceFlowUseCaseImpl = get(named("manager_get"))
        )
    }

    viewModel { parameters ->
        TasksViewModel(
            state = parameters.get(),
            insertTaskUseCaseImpl = get(named("task_insert")),
            updateTaskUseCaseImpl = get(named("task_update")),
            deleteTaskUseCaseImpl = get(named("task_delete")),
            deleteAllCompletedTasksImpl = get(named("task_delete_all")),
            getTasksUseCaseImpl = get(named("task_get")),
            updateSortOrderUseCaseImpl = get(named("manager_sort")),
            updateHideCompletedUseCaseImpl = get(named("manager_hide")),
            getPreferenceFlowUseCaseImpl = get(named("manager_get"))
        )
    }

    viewModel { parameters ->
        AddEditNoteViewModel(
            savedStateHandle = parameters.get(),
            insertNoteUseCaseImpl = get(named("note_insert")),
            updateNoteUseCaseImpl = get(named("note_update"))
        )
    }

    viewModel { parameters ->
        AddEditTaskViewModel(
            state = parameters.get(),
            insertTaskUseCaseImpl = get(named("task_insert")),
            updateTaskUseCaseImpl = get(named("task_update"))
        )
    }
}