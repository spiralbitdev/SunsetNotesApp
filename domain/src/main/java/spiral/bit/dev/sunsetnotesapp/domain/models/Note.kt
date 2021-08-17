package spiral.bit.dev.sunsetnotesapp.domain.models

import java.text.DateFormat

data class Note(
    val id: Int = 0,
    val title: String,
    val subTitle: String,
    val contentText: String = "",
    val createdDate: Long = System.currentTimeMillis(),
    val imageUri: String = ""
) {
    val createdFormattedDate: String
        get() = DateFormat.getDateTimeInstance().format(createdDate)
}