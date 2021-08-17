package spiral.bit.dev.sunsetnotesapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
@Entity(tableName = "tasks", indices = [Index(value = ["task_id"], unique = true)])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    val id: Int = 0,
    val taskName: String,
    val isTaskImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val createdTime: Long = System.currentTimeMillis()
) : Parcelable {
    val createdFormattedTime: String
        get() = DateFormat.getDateTimeInstance().format(createdTime)
}