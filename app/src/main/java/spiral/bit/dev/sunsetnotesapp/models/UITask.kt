package spiral.bit.dev.sunsetnotesapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
data class UITask(
    val id: Int = 0,
    val taskName: String,
    val isTaskImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val createdTime: Long = System.currentTimeMillis()
) : Parcelable {
    val createdFormattedTime: String
        get() = DateFormat.getDateTimeInstance().format(createdTime)
}