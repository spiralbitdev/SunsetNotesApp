package spiral.bit.dev.sunsetnotesapp.data.repositories

import kotlinx.coroutines.flow.Flow
import spiral.bit.dev.sunsetnotesapp.data.db.NoteDao
import spiral.bit.dev.sunsetnotesapp.data.mappers.toFlowOfNotes
import spiral.bit.dev.sunsetnotesapp.data.mappers.toNoteEntity
import spiral.bit.dev.sunsetnotesapp.domain.models.Note
import spiral.bit.dev.sunsetnotesapp.domain.models.SortOrder
import spiral.bit.dev.sunsetnotesapp.domain.repositories.INoteRepository

class NotesRepositoryImpl(private val noteDao: NoteDao) : INoteRepository {

    override suspend fun insert(note: Note) {
        val mappedNote = note.toNoteEntity()
        noteDao.insert(mappedNote)
    }

    override suspend fun update(note: Note) {
        val mappedNote = note.toNoteEntity()
        noteDao.update(mappedNote.copy())
    }

    override suspend fun delete(note: Note) {
        val mappedNote = note.toNoteEntity()
        noteDao.delete(mappedNote)
    }

    override fun get(searchQuery: String, sortOrder: SortOrder): Flow<List<Note>> {
        return when (sortOrder) {
            SortOrder.BY_NAME -> noteDao.getAllNotesSortedByName(searchQuery).toFlowOfNotes()
            SortOrder.BY_DATE -> noteDao.getAllNotesSortedByDate(searchQuery).toFlowOfNotes()
        }
    }
}