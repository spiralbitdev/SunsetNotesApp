package spiral.bit.dev.sunsetnotesapp.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.sunsetnotesapp.databinding.TaskItemBinding
import spiral.bit.dev.sunsetnotesapp.models.UITask

class TasksAdapter(private val listener: OnItemClickListener) :
    ListAdapter<UITask, TasksAdapter.TodoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TaskItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class TodoViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding) {
                root.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onTaskClicked(task)
                    }
                }
                checkBoxCompleted.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClicked(task, checkBoxCompleted.isChecked)
                    }
                }
            }
        }

        fun bind(task: UITask) {
            with(binding) {
                textViewTitle.text = task.taskName
                checkBoxCompleted.isChecked = task.isCompleted
                labelPriority.isVisible = task.isTaskImportant
                textViewTitle.paint.isStrikeThruText = task.isCompleted
            }
        }
    }

    interface OnItemClickListener {
        fun onTaskClicked(task: UITask)
        fun onCheckBoxClicked(task: UITask, isChecked: Boolean)
    }

    class DiffCallback : DiffUtil.ItemCallback<UITask>() {
        override fun areItemsTheSame(oldItem: UITask, newItem: UITask) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UITask, newItem: UITask) =
            oldItem == newItem
    }
}