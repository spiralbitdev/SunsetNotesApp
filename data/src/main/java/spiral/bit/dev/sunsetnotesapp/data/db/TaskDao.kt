package spiral.bit.dev.sunsetnotesapp.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.data.models.TaskEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskEntity: TaskEntity)

    @Update
    suspend fun update(taskEntity: TaskEntity)

    @Delete
    suspend fun delete(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isTaskImportant DESC, taskName")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isTaskImportant DESC, createdTime")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<TaskEntity>>

    @Query("DELETE FROM tasks WHERE isCompleted == 1")
    suspend fun deleteAllCompletedTasks()
}