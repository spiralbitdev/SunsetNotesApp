package spiral.bit.dev.sunsetnotesapp.ui.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.databinding.FragmentNotesBinding
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.models.UINote
import spiral.bit.dev.sunsetnotesapp.util.action
import spiral.bit.dev.sunsetnotesapp.util.onQueryTextChanged
import spiral.bit.dev.sunsetnotesapp.util.snack

@FlowPreview
@ExperimentalCoroutinesApi
class NotesFragment : Fragment(R.layout.fragment_notes),
    NotesAdapter.OnItemClickListener {

    private lateinit var searchView: SearchView
    private val viewModel by stateViewModel<NotesViewModel>()
    private val notesAdapter = NotesAdapter(this)
    private val binding: FragmentNotesBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpObservers()

        setUpViews()
        setHasOptionsMenu(true)
    }

    private fun setUpViews() = with(binding) {
        notesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = notesAdapter

            ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ) = false

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val note = notesAdapter.currentList[viewHolder.absoluteAdapterPosition]
                        viewModel.onNoteSwiped(note)
                    }
                }).attachToRecyclerView(this)
        }

        fabAddNote.setOnClickListener {
            viewModel.onAddNewNoteClicked()
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            bundle.getInt("add_edit_result").apply {
                viewModel.onAddEditResult(this)
            }
        }
    }

    private fun setUpObservers() {
        viewModel.notesFlow
            .onEach { notes -> notesAdapter.submitList(notes) }
            .flowWithLifecycle(lifecycle)

        viewModel.noteEvents.onEach { events ->
            when (events) {
                is NotesViewModel.NoteEvents.NavigateToAddNoteScreen -> {
                    NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                        null, getString(R.string.add_note_label)
                    ).apply {
                        findNavController().navigate(this)
                    }
                }
                is NotesViewModel.NoteEvents.NavigateToEditNoteScreen -> {
                    NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                        events.note, getString(R.string.edit_note_label)
                    ).apply {
                        findNavController().navigate(this)
                    }
                }
                is NotesViewModel.NoteEvents.ShowNoteSavedMsg -> {
                    binding.root.snack(R.string.note_successfully_saved)
                }
                is NotesViewModel.NoteEvents.ShowUndoDeleteSnackbar -> {
                    binding.root.snack(R.string.note_deleted, Snackbar.LENGTH_SHORT) {
                        action(R.string.cancel_label) {
                            viewModel.onUndoDelete(events.note)
                        }
                    }
                }
            }
        }.flowWithLifecycle(lifecycle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)

        menu.apply {
            findItem(R.id.action_hide_completed_tasks)
                .isVisible = false
            findItem(R.id.action_delete_all_completed_tasks)
                .isVisible = false
        }

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

        val pendingQuery = viewModel._searchQueryFlow.value
        if (pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged { msg ->
            viewModel._searchQueryFlow.value = msg
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }

    override fun onNoteClicked(note: UINote) {
        viewModel.onNoteSelected(note)
    }
}