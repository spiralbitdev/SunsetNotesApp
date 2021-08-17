package spiral.bit.dev.sunsetnotesapp.data.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import spiral.bit.dev.sunsetnotesapp.data.models.TaskEntity
import spiral.bit.dev.sunsetnotesapp.domain.models.Task

fun Task.toTaskEntity() = TaskEntity(
    id, taskName, isTaskImportant, isCompleted, createdTime
)

fun TaskEntity.toTask() = Task(
    id, taskName, isTaskImportant, isCompleted, createdTime
)

fun Flow<List<TaskEntity>>.toFlowOfTasks(): Flow<List<Task>> {
    val tempList = mutableListOf<Task>()
    map { notesEntityList ->
        notesEntityList.forEach { noteEntity ->
            tempList.add(noteEntity.toTask())
        }
    }
    return flowOf(tempList)
}