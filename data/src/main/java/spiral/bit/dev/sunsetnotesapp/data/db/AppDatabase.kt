package spiral.bit.dev.sunsetnotesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import spiral.bit.dev.sunsetnotesapp.data.models.NoteEntity
import spiral.bit.dev.sunsetnotesapp.data.models.TaskEntity

@Database(entities = [NoteEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao
}