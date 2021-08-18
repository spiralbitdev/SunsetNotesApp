package spiral.bit.dev.sunsetnotesapp.ui.tasks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import spiral.bit.dev.sunsetnotesapp.data.mappers.mapItems
import spiral.bit.dev.sunsetnotesapp.data.mappers.toTask
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.usecases.GetPreferenceFlowUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.UpdateSortOrderUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations.*
import spiral.bit.dev.sunsetnotesapp.mappers.toFlowOfUITasks
import spiral.bit.dev.sunsetnotesapp.mappers.toTask
import spiral.bit.dev.sunsetnotesapp.mappers.toUITask
import spiral.bit.dev.sunsetnotesapp.models.UITask
import spiral.bit.dev.sunsetnotesapp.util.ADD_RESULT_OK
import spiral.bit.dev.sunsetnotesapp.util.EDIT_RESULT_OK

@FlowPreview
@ExperimentalCoroutinesApi
class TasksViewModel(
    private val state: SavedStateHandle,
    private val insertTaskUseCaseImpl: InsertTaskUseCaseImpl,
    private val updateTaskUseCaseImpl: UpdateTaskUseCaseImpl,
    private val deleteTaskUseCaseImpl: DeleteTaskUseCaseImpl,
    private val deleteAllCompletedTasksImpl: DeleteAllCompletedTasksImpl,
    private val getTasksUseCaseImpl: GetTasksUseCaseImpl,
    private val updateSortOrderUseCaseImpl: UpdateSortOrderUseCaseImpl,
    private val updateHideCompletedUseCaseImpl: UpdateHideCompletedUseCaseImpl,
    private val getPreferenceFlowUseCaseImpl: GetPreferenceFlowUseCaseImpl
) : ViewModel() {

    var searchQuery = state.get<String>("searchQuery") ?: ""
        set(value) {
            field = value
            state.set("searchQuery", value)
        }

    private val taskEventsChannel = Channel<TasksEvents>()
    val taskEvents = taskEventsChannel.receiveAsFlow()

    val _preferenceFlow = getPreferenceFlowUseCaseImpl.getPreferenceFlow()
    private val preferenceFlow get() = _preferenceFlow

    val _searchQueryFlow = MutableStateFlow(searchQuery)
    private val searchQueryFlow: StateFlow<String> = _searchQueryFlow

    val tasksFlow = preferenceFlow.zip(searchQueryFlow) { filterPrefs, query ->
        getTasksUseCaseImpl.get(query, filterPrefs.sortOrder, filterPrefs.hideCompleted)
            .mapItems { it.toUITask() }
    }.flattenConcat()

    fun onTaskSwiped(task: UITask) = viewModelScope.launch {
        task.toTask().run {
            deleteTaskUseCaseImpl.delete(this)
            taskEventsChannel.send(TasksEvents.ShowUndoDeleteSnackbar(task))
        }
    }

    fun onAddNewTaskClicked() = viewModelScope.launch {
        taskEventsChannel.send(TasksEvents.NavigateToAddTaskScreen)
    }

    fun onAddEditResult(result: Int) = viewModelScope.launch {
        when (result) {
            ADD_RESULT_OK -> taskEventsChannel.send(TasksEvents.ShowSavedTaskMsg("Задача создана"))
            EDIT_RESULT_OK -> taskEventsChannel.send(TasksEvents.ShowSavedTaskMsg("Задача отредактирована"))
        }
    }

    fun onUndoDelete(task: UITask) = viewModelScope.launch {
        task.toTask().run {
            insertTaskUseCaseImpl.insert(this)
        }
    }

    fun onTaskClicked(task: UITask) = viewModelScope.launch {
        taskEventsChannel.send(TasksEvents.NavigateToEditTaskScreen(task))
    }

    fun onCheckBoxClicked(task: UITask, isChecked: Boolean) = viewModelScope.launch {
        task.toTask().run {
            updateTaskUseCaseImpl.update(copy(isCompleted = isChecked))
        }
    }

    fun onSortOrderSelect(order: SortOrder) = viewModelScope.launch {
        updateSortOrderUseCaseImpl.updateSortOrder(order)
    }

    fun onHideCompletedSelect(hideCompleted: Boolean) = viewModelScope.launch {
        updateHideCompletedUseCaseImpl.updateHideCompleted(hideCompleted)
    }

    fun onDeleteAllCompleted() = viewModelScope.launch(Dispatchers.IO) {
        deleteAllCompletedTasksImpl.deleteAllCompletedTasks()
    }

    sealed class TasksEvents {
        data class ShowUndoDeleteSnackbar(val task: UITask) : TasksEvents()
        data class NavigateToEditTaskScreen(val task: UITask) : TasksEvents()
        data class ShowSavedTaskMsg(val msg: String) : TasksEvents()
        object NavigateToAddTaskScreen : TasksEvents()
    }
}