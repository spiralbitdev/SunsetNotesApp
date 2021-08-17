package spiral.bit.dev.sunsetnotesapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
data class UINote(
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