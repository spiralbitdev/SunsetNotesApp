package spiral.bit.dev.sunsetnotesapp.ui.add_edit.notes

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.databinding.FragmentAddEditNoteBinding
import spiral.bit.dev.sunsetnotesapp.util.MIME_TYPE_IMAGE
import spiral.bit.dev.sunsetnotesapp.util.VoicePicker
import spiral.bit.dev.sunsetnotesapp.util.hasPermissions
import spiral.bit.dev.sunsetnotesapp.util.infixLoad

class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private val noteViewModel: AddEditNoteViewModel by stateViewModel()
    private val binding: FragmentAddEditNoteBinding by viewBinding()
    private var isReadModeEnabled = false

    private val getVoicePicker = registerForActivityResult(VoicePicker()) {
        buildString {
            append(binding.inputNote.text.toString())
            append(" ")
            append(it)
        }.also { binding.inputNote.setText(it) }
    }

    private val getPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            requireContext().hasPermissions(permissions[0])
            requireContext().hasPermissions(permissions[1])
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            with(binding) {
                imageNote.isVisible = true
                imgRemoveImage.isVisible = true
                imageNote infixLoad imageUri.toString()
            }.also { noteViewModel.imageUri = imageUri.toString() }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            inputNoteTitle.setText(noteViewModel.note?.title)
            inputNote.setText(noteViewModel.note?.contentText)
            inputNoteSubTitle.setText(noteViewModel.note?.subTitle)
            textDateTime.text = noteViewModel.note?.createdFormattedDate
            if (noteViewModel.imageUri.isNotEmpty()) {
                imageNote.isVisible = true
                imgRemoveImage.isVisible = true
                imageNote infixLoad noteViewModel.imageUri
            } else {
                imageNote.isVisible = false
                imgRemoveImage.isVisible = false
            }

            setUpListeners()
            setUpObservers()
        }
    }

    private fun setUpListeners() = with(binding) {
        fabImage.setOnClickListener {
            if (requireContext().hasPermissions(permissions[0]))
                getPermissions.launch(permissions)
            else getContent.launch(MIME_TYPE_IMAGE)
        }

        fabVoice.setOnClickListener {
            if (requireContext().hasPermissions(permissions[1]))
                getPermissions.launch(permissions)
            else getVoicePicker.launch()
        }

        fabSave.setOnClickListener {
            noteViewModel.saveNoteClicked(
                inputNote.text.toString(),
                inputNoteSubTitle.text.toString(),
                inputNote.text.toString()
            )
        }

        imgRemoveImage.setOnClickListener {
            noteViewModel.imageUri = ""
            imageNote.isVisible = false
            imgRemoveImage.isVisible = false
        }

        imageReadMode.setOnClickListener {
            toggleReadMode()
        }
    }

    private fun setUpObservers() {
        noteViewModel.noteEvents.onEach { event ->
            when (event) {
                is AddEditNoteEvents.NavigateBackWithResult -> {
                    setFragmentResult(
                        "add_edit_request",
                        bundleOf("add_edit_result" to event.result)
                    )
                    findNavController().popBackStack()
                }
                is AddEditNoteEvents.ShowInvalidInputMsg -> {
                    Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun toggleReadMode() {
        with(binding) {
            inputNoteSubTitle.isEnabled = !isReadModeEnabled
            inputNoteTitle.isEnabled = !isReadModeEnabled
            inputNote.isEnabled = !isReadModeEnabled
            if (!isReadModeEnabled) imageReadMode.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorIcons
                )
            )
            else imageReadMode.setColorFilter(Color.YELLOW)
        }.also { isReadModeEnabled = !isReadModeEnabled }
    }

    companion object {
        private val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
        )
    }
}