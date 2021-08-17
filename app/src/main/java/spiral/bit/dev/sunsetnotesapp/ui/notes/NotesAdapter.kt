package spiral.bit.dev.sunsetnotesapp.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.sunsetnotesapp.util.infixLoad
import spiral.bit.dev.sunsetnotesapp.databinding.NoteItemBinding
import spiral.bit.dev.sunsetnotesapp.models.UINote

class NotesAdapter(private val listener: OnItemClickListener) :
    ListAdapter<UINote, NotesAdapter.NoteViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding) {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val note = getItem(position)
                        listener.onNoteClicked(note)
                    }
                }
            }
        }

        fun bind(note: UINote) {
            with(binding) {
                textTitle.text = note.title
                textSubTitle.text = note.subTitle
                textDateTime.text = note.createdFormattedDate
                if (note.imageUri.isNotEmpty()) {
                    imageNote.isVisible = true
                    imageNote infixLoad note.imageUri
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<UINote>() {
        override fun areItemsTheSame(oldItem: UINote, newItem: UINote) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UINote, newItem: UINote) =
            oldItem == newItem
    }

    interface OnItemClickListener {
        fun onNoteClicked(note: UINote)
    }
}