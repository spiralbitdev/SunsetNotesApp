package spiral.bit.dev.sunsetnotesapp.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(text: String) {
    Snackbar.make(this, text, Snackbar.LENGTH_LONG).show()
}