package spiral.bit.dev.sunsetnotesapp.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.databinding.FragmentTasksBinding
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.models.UITask
import spiral.bit.dev.sunsetnotesapp.util.onQueryTextChanged
import spiral.bit.dev.sunsetnotesapp.util.showSnackbar

@FlowPreview
@ExperimentalCoroutinesApi
class TasksFragment : Fragment(R.layout.fragment_tasks), TasksAdapter.OnItemClickListener {

    private lateinit var searchView: SearchView
    private val viewModel: TasksViewModel by stateViewModel()
    private val tasksAdapter = TasksAdapter(this)
    private val binding: FragmentTasksBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tasksRecyclerView.apply {
                setHasFixedSize(true)
                adapter = tasksAdapter
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = tasksAdapter.currentList[viewHolder.absoluteAdapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(tasksRecyclerView)

            fabAddTask.setOnClickListener {
                viewModel.onAddNewTaskClicked()
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        setUpObservers()
        setHasOptionsMenu(true)
    }

    private fun setUpObservers() {
        viewModel.tasksFlow.onEach { tasks -> tasksAdapter.submitList(tasks) }
            .launchIn(lifecycleScope)

        viewModel.taskEvents.onEach { event ->
            when (event) {
                TasksViewModel.TasksEvents.NavigateToAddTaskScreen -> {
                    val action =
                        TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                            null, getString(R.string.task_add_label)
                        )
                    findNavController().navigate(action)
                }
                is TasksViewModel.TasksEvents.NavigateToEditTaskScreen -> {
                    val action =
                        TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                            event.task, getString(R.string.task_edit_label)
                        )
                    findNavController().navigate(action)
                }
                is TasksViewModel.TasksEvents.ShowSavedTaskMsg -> {
                    binding.root.showSnackbar(getString(R.string.task_successfully_saved))
                }
                is TasksViewModel.TasksEvents.ShowUndoDeleteSnackbar -> {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.task_deleted),
                        Snackbar.LENGTH_SHORT
                    ).setAction(getString(R.string.cancel_label)) {
                        viewModel.onUndoDelete(event.task)
                    }.show()
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel._searchQueryFlow.value
        if (pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel._searchQueryFlow.value = it
        }

        lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel._preferenceFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelect(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelect(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedSelect(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                viewModel.onDeleteAllCompleted()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (this::searchView.isInitialized)
            searchView.setOnQueryTextListener(null)
    }

    override fun onTaskClicked(task: UITask) {
        viewModel.onTaskClicked(task)
    }

    override fun onCheckBoxClicked(task: UITask, isChecked: Boolean) {
        viewModel.onCheckBoxClicked(task, isChecked)
    }
}