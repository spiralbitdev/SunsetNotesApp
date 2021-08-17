package spiral.bit.dev.sunsetnotesapp.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import spiral.bit.dev.sunsetnotesapp.models.UITask
import spiral.bit.dev.sunsetnotesapp.domain.models.Task

fun Task.toUITask() = UITask(
    id, taskName, isTaskImportant, isCompleted, createdTime
)

fun UITask.toTask() = Task(
    id, taskName, isTaskImportant, isCompleted, createdTime
)

fun Flow<List<Task>>.toFlowOfUITasks(): Flow<List<UITask>> {
    val tempList = mutableListOf<UITask>()
    map { notesList ->
        notesList.forEach { note ->
            tempList.add(note.toUITask())
        }
    }
    return flowOf(tempList)
}