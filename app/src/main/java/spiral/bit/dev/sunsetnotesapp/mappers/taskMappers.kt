package spiral.bit.dev.sunsetnotesapp.mappers

import spiral.bit.dev.sunsetnotesapp.domain.models.Task
import spiral.bit.dev.sunsetnotesapp.models.UITask

fun Task.toUITask() = UITask(
    id, taskName, isTaskImportant, isCompleted, createdTime
)

fun UITask.toTask() = Task(
    id, taskName, isTaskImportant, isCompleted, createdTime
)