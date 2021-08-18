package spiral.bit.dev.sunsetnotesapp.util

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG
) = Snackbar.make(this, messageRes, length).show()

inline fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit
) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun Snackbar.action(@StringRes actionRes: Int, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), listener)
}

fun Snackbar.action(action: String, listener: (View) -> Unit) {
    setAction(action, listener)
}