package spiral.bit.dev.sunsetnotesapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject
import spiral.bit.dev.sunsetnotesapp.data.db.AppDatabase
import spiral.bit.dev.sunsetnotesapp.data.db.NoteDao
import spiral.bit.dev.sunsetnotesapp.data.db.TaskDao
import spiral.bit.dev.sunsetnotesapp.data.managers.PreferenceManager
import spiral.bit.dev.sunsetnotesapp.data.models.NoteEntity
import spiral.bit.dev.sunsetnotesapp.data.models.TaskEntity
import spiral.bit.dev.sunsetnotesapp.data.repositories.NotesRepositoryImpl
import spiral.bit.dev.sunsetnotesapp.data.repositories.TaskRepositoryImpl
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
import spiral.bit.dev.sunsetnotesapp.ui.add_edit.notes.AddEditNoteViewModel
import spiral.bit.dev.sunsetnotesapp.ui.add_edit.tasks.AddEditTaskViewModel
import spiral.bit.dev.sunsetnotesapp.ui.notes.NotesViewModel
import spiral.bit.dev.sunsetnotesapp.ui.tasks.TasksViewModel

val dataModule = module {
    single(createdAtStart = true) { get<AppDatabase>().noteDao() }
    single(createdAtStart = true) { get<AppDatabase>().taskDao() }
    single(createdAtStart = true) {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_db"
        ).fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val app by inject<AppDatabase>()
                    val noteDao = app.noteDao()
                    val taskDao = app.taskDao()
                    CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
                        noteDao.run {
                            insert(NoteEntity(title = "Помыть тарелки", subTitle = "Дела по дому"))
                            insert(
                                NoteEntity(
                                    title = "Пропылесосить квартиру",
                                    subTitle = "Дела по дому"
                                )
                            )
                            insert(NoteEntity(title = "Сходить в школу", subTitle = "Срочно"))
                            insert(
                                NoteEntity(
                                    title = "Купить детям поесть",
                                    subTitle = "Не забыть!!!"
                                )
                            )
                            insert(
                                NoteEntity(
                                    title = "Лекция философия",
                                    subTitle = "Нужно дописать"
                                )
                            )
                        }

                        taskDao.run {
                            insert(
                                TaskEntity(
                                    taskName = "Отвести сына в детсад",
                                    isCompleted = true,
                                    isTaskImportant = true
                                )
                            )
                            insert(
                                TaskEntity(
                                    taskName = "Убраться в квартире",
                                    isTaskImportant = true
                                )
                            )
                            insert(TaskEntity(taskName = "Сходить в душ"))
                            insert(TaskEntity(taskName = "Прочитать книгу"))
                            insert(
                                TaskEntity(
                                    taskName = "Сходить на работу",
                                    isTaskImportant = true
                                )
                            )
                        }
                    }
                }
            })
            .build()
    }
}

val repositoryModule = module {
    single<INoteRepository> { provideNotesRepository(noteDao = get()) }
    single<ITaskRepository> { provideTaskRepository(taskDao = get()) }
    single<IPreferenceManager> { providePreferenceManager(context = androidContext()) }
}

fun provideNotesRepository(noteDao: NoteDao) = NotesRepositoryImpl(noteDao)

fun provideTaskRepository(taskDao: TaskDao) = TaskRepositoryImpl(taskDao)

fun providePreferenceManager(context: Context) = PreferenceManager(context)

val useCaseModule = module {

    //notes
    single(named("note_insert")) {
        InsertNoteUseCaseImpl(notesRepository = get())
    }
    single(named("note_update")) {
        UpdateNoteUseCaseImpl(notesRepository = get())
    }
    single(named("note_delete")) {
        DeleteNoteUseCaseImpl(notesRepository = get())
    }
    single(named("note_get")) {
        GetNotesUseCaseImpl(notesRepository = get())
    }

    //tasks
    single(named("task_insert")) {
        InsertTaskUseCaseImpl(taskRepository = get())
    }
    single(named("task_update")) {
        UpdateTaskUseCaseImpl(taskRepository = get())
    }
    single(named("task_delete")) {
        DeleteTaskUseCaseImpl(taskRepository = get())
    }
    single(named("task_delete_all")) {
        DeleteAllCompletedTasksImpl(taskRepository = get())
    }
    single(named("task_get")) {
        GetTasksUseCaseImpl(taskRepository = get())
    }

    //manager
    single(named("manager_sort")) {
        UpdateSortOrderUseCaseImpl(preferenceManager = get())
    }
    single(named("manager_hide")) {
        UpdateHideCompletedUseCaseImpl(preferenceManager = get())
    }
    single(named("manager_get")) {
        GetPreferenceFlowUseCaseImpl(preferenceManager = get())
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
val viewModelModule = module {
    viewModel { parameters ->
        NotesViewModel(
            state = parameters.get(),
            insertNoteUseCaseImpl = get(named("note_insert")),
            updateSortOrderUseCaseImpl = get(named("manager_sort")),
            deleteNoteUseCaseImpl = get(named("note_delete")),
            getNotesUseCaseImpl = get(named("note_get")),
            getPreferenceFlowUseCaseImpl = get(named("manager_get"))
        )
    }

    viewModel { parameters ->
        TasksViewModel(
            state = parameters.get(),
            insertTaskUseCaseImpl = get(named("task_insert")),
            updateTaskUseCaseImpl = get(named("task_update")),
            deleteTaskUseCaseImpl = get(named("task_delete")),
            deleteAllCompletedTasksImpl = get(named("task_delete_all")),
            getTasksUseCaseImpl = get(named("task_get")),
            updateSortOrderUseCaseImpl = get(named("manager_sort")),
            updateHideCompletedUseCaseImpl = get(named("manager_hide")),
            getPreferenceFlowUseCaseImpl = get(named("manager_get"))
        )
    }

    viewModel { parameters ->
        AddEditNoteViewModel(
            savedStateHandle = parameters.get(),
            insertNoteUseCaseImpl = get(named("note_insert")),
            deleteNoteUseCaseImpl = get(named("note_delete")),
            updateNoteUseCaseImpl = get(named("note_update"))
        )
    }

    viewModel { parameters ->
        AddEditTaskViewModel(
            state = parameters.get(),
            insertTaskUseCaseImpl = get(named("task_insert")),
            updateTaskUseCaseImpl = get(named("task_update"))
        )
    }
}