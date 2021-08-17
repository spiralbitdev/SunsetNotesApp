package spiral.bit.dev.sunsetnotesapp

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import spiral.bit.dev.sunsetnotesapp.di.dataModule
import spiral.bit.dev.sunsetnotesapp.di.repositoryModule
import spiral.bit.dev.sunsetnotesapp.di.useCaseModule
import spiral.bit.dev.sunsetnotesapp.di.viewModelModule

@ExperimentalCoroutinesApi
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@App)
            modules(dataModule, repositoryModule, viewModelModule, useCaseModule)
        }
    }
}