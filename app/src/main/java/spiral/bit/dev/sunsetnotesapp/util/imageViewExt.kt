package spiral.bit.dev.sunsetnotesapp.util

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

infix fun ImageView.infixLoad(imageUri: String) =
    Glide.with(this)
        .load(Uri.parse(imageUri))
        .into(this)