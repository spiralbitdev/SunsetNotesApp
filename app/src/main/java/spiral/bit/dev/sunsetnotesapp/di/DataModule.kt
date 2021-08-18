package spiral.bit.dev.sunsetnotesapp.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import spiral.bit.dev.sunsetnotesapp.data.db.AppDatabase
import spiral.bit.dev.sunsetnotesapp.data.models.NoteEntity
import spiral.bit.dev.sunsetnotesapp.data.models.TaskEntity

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