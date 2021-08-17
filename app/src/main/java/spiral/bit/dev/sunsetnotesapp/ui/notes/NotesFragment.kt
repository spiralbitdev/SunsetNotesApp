package spiral.bit.dev.sunsetnotesapp.ui.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import spiral.bit.dev.sunsetnotesapp.R
import spiral.bit.dev.sunsetnotesapp.databinding.FragmentNotesBinding
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.models.UINote
import spiral.bit.dev.sunsetnotesapp.util.onQueryTextChanged
import spiral.bit.dev.sunsetnotesapp.util.showSnackbar

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

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(requireContext(), result)
        }

        setUpViews()
        setUpObservers()
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
    }

    private fun setUpObservers() {
        viewModel.notesFlow
            .onEach { notes -> notesAdapter.submitList(notes) }
            .launchIn(lifecycleScope)

        viewModel.noteEvents.onEach { events ->
            when (events) {
                is NotesViewModel.NoteEvents.NavigateToAddNoteScreen -> {
                    val action =
                        NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                            null, getString(R.string.add_note_label)
                        )
                    findNavController().navigate(action)
                }
                is NotesViewModel.NoteEvents.NavigateToEditNoteScreen -> {
                    val action =
                        NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                            events.note, getString(R.string.edit_note_label)
                        )
                    findNavController().navigate(action)
                }
                is NotesViewModel.NoteEvents.ShowNoteSavedMsg -> {
                    binding.root.showSnackbar(getString(R.string.note_successfully_saved))
                }
                is NotesViewModel.NoteEvents.ShowUndoDeleteSnackbar -> {
                    Snackbar.make(
                        requireView(),
                        getString(R.string.note_deleted),
                        Snackbar.LENGTH_SHORT
                    ).setAction(getString(R.string.cancel_label)) {
                        viewModel.onUndoDelete(events.note)
                    }.show()
                }
            }
        }.launchIn(lifecycleScope)
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
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel._searchQueryFlow.value
        if (pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel._searchQueryFlow.value = it
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