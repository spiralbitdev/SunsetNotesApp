package spiral.bit.dev.sunsetnotesapp.domain.models

import java.text.DateFormat

data class Task(
    val id: Int = 0,
    val taskName: String,
    val isTaskImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val createdTime: Long = System.currentTimeMillis()
)  {
    val createdFormattedTime: String
        get() = DateFormat.getDateTimeInstance().format(createdTime)
}