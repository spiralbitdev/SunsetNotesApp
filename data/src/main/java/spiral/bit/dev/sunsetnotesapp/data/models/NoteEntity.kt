package spiral.bit.dev.sunsetnotesapp.data.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
@Entity(tableName = "notes", indices = [Index(value = ["note_id"], unique = true)])
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Int = 0,
    val title: String,
    val subTitle: String,
    val contentText: String = "",
    val createdDate: Long = System.currentTimeMillis(),
    val imageUri: String = ""
) : Parcelable {
    val createdFormattedDate: String
        get() = DateFormat.getDateTimeInstance().format(createdDate)
}