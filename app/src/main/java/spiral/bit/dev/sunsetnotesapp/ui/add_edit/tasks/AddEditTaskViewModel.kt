package spiral.bit.dev.sunsetnotesapp.ui.add_edit.tasks

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.domain.models.Task
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations.InsertTaskUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations.UpdateTaskUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.util.ADD_RESULT_OK
import spiral.bit.dev.sunsetnotesapp.util.EDIT_RESULT_OK

class AddEditTaskViewModel(
    private val state: SavedStateHandle,
    private val insertTaskUseCaseImpl: InsertTaskUseCaseImpl,
    private val updateTaskUseCaseImpl: UpdateTaskUseCaseImpl
) : ViewModel() {

    val task = state.get<Task>("task")
    private val taskEventsChannel = Channel<AddEditTaskEvents>()
    val taskEvents = taskEventsChannel.receiveAsFlow()

    var taskName = state.get<String>("taskName") ?: task?.taskName ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.isTaskImportant ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    fun onSaveClick(context: Context) = viewModelScope.launch {
        if (taskName.isBlank()) {
            taskEventsChannel.send(
                AddEditTaskEvents.ShowInvalidInputMsg(
                    context.getString(R.string.task_name_can_t_be_empty)
                )
            )
            return@launch
        }

        if (task != null) {
            val updatedTask = task.copy(taskName = taskName, isTaskImportant = taskImportance)
            updateTask(updatedTask)
        } else {
            val newTask = Task(taskName = taskName, isTaskImportant = taskImportance)
            createTask(newTask)
        }
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        insertTaskUseCaseImpl.insert(task)
        taskEventsChannel.send(AddEditTaskEvents.NavigateBackWithResult(ADD_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        updateTaskUseCaseImpl.update(task)
        taskEventsChannel.send(AddEditTaskEvents.NavigateBackWithResult(EDIT_RESULT_OK))
    }

    sealed class AddEditTaskEvents {
        data class ShowInvalidInputMsg(val msg: String) : AddEditTaskEvents()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvents()
    }
}