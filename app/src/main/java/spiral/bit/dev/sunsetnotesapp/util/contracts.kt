package spiral.bit.dev.sunsetnotesapp.util

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import spiral.bit.dev.sunsetnotesapp.R
import java.util.*

class VoicePicker : ActivityResultContract<Unit, String>() {

    override fun createIntent(context: Context, input: Unit?) = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                context.getString(R.string.add_voice_speak_label)
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val result =
            requireNotNull(intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS))
        return buildString {
            result.forEach { item ->
                append(item.replace("[]", " "))
            }
        }
    }
}