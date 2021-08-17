package spiral.bit.dev.sunsetnotesapp.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

fun Context.hasPermissions(permission: String) = ActivityCompat.checkSelfPermission(
    this, permission
) == PackageManager.PERMISSION_GRANTED