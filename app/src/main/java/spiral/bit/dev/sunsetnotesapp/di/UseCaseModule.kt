package spiral.bit.dev.sunsetnotesapp.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import spiral.bit.dev.sunsetnotesapp.domain.managers.IPreferenceManager
import spiral.bit.dev.sunsetnotesapp.domain.repositories.INoteRepository
import spiral.bit.dev.sunsetnotesapp.domain.repositories.ITaskRepository
import spiral.bit.dev.sunsetnotesapp.domain.usecases.GetPreferenceFlowUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.UpdateSortOrderUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.DeleteNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.GetNotesUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.InsertNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.notes.implementations.UpdateNoteUseCaseImpl
import spiral.bit.dev.sunsetnotesapp.domain.usecases.tasks.implementations.*


