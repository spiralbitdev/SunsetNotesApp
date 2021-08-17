package spiral.bit.dev.sunsetnotesapp.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.data.models.NoteEntity

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteEntity: NoteEntity)

    @Update
    suspend fun update(noteEntity: NoteEntity)

    @Delete
    suspend fun delete(noteEntity: NoteEntity)

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' ORDER BY note_id DESC, title")
    fun getAllNotesSortedByName(searchQuery: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' ORDER BY note_id DESC, createdDate")
    fun getAllNotesSortedByDate(searchQuery: String): Flow<List<NoteEntity>>
}