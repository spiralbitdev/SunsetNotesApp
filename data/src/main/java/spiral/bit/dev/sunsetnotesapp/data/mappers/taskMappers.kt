package spiral.bit.dev.sunsetnotesapp.data.mappers

import spiral.bit.dev.sunsetnotesapp.data.models.TaskEntity
import spiral.bit.dev.sunsetnotesapp.domain.models.Task

fun Task.toTaskEntity() = TaskEntity(
    id, taskName, isTaskImportant, isCompleted, createdTime
)

fun TaskEntity.toTask() = Task(
    id, taskName, isTaskImportant, isCompleted, createdTime
)