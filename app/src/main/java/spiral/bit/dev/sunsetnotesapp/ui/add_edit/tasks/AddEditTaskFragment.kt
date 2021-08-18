package spiral.bit.dev.sunsetnotesapp.ui.add_edit.tasks

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.databinding.FragmentAddEditTaskBinding
import spiral.bit.dev.sunsetnotesapp.util.snack

class AddEditTaskFragment : Fragment(R.layout.fragment_add_edit_task) {

    private val viewModel: AddEditTaskViewModel by stateViewModel()
    private val binding: FragmentAddEditTaskBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()
        setUpListeners()
        setUpObservers()
    }

    private fun setUpViews() = with(binding) {
        editTextTaskName.setText(viewModel.taskName)
        checkBoxImportant.isChecked = viewModel.taskImportance
        checkBoxImportant.jumpDrawablesToCurrentState()
        textViewDateCreated.text = viewModel.task?.createdFormattedTime
        textViewDateCreated.isVisible = viewModel.task != null
        textViewDateCreated.text = String.format(
            "Created: %s",
            viewModel.task?.createdFormattedTime
        )
    }

    private fun setUpListeners() = with(binding) {
        editTextTaskName.addTextChangedListener {
            viewModel.taskName = it.toString()
        }

        checkBoxImportant.setOnCheckedChangeListener { _, isChecked ->
            viewModel.taskImportance = isChecked
        }

        fabSaveTask.setOnClickListener {
            viewModel.onSaveClick()
        }
    }

    private fun setUpObservers() = lifecycleScope.launchWhenStarted {
        viewModel.taskEvents.collect { event ->
            when (event) {
                is AddEditTaskViewModel.AddEditTaskEvents.NavigateBackWithResult -> {
                    setFragmentResult(
                        "add_edit_request",
                        bundleOf("add_edit_result" to event.result)
                    ).apply {
                        findNavController().popBackStack()
                    }
                }
                is AddEditTaskViewModel.AddEditTaskEvents.ShowInvalidInputMsg -> {
                    binding.root.snack(event.msg)
                }
            }
        }
    }
}