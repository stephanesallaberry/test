

package fr.stephanesallaberry.news.android

import android.app.Application
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import fr.stephanesallaberry.news.android.di.interactorsModule
import fr.stephanesallaberry.news.android.di.networkModule
import fr.stephanesallaberry.news.android.di.providersModule
import fr.stephanesallaberry.news.android.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class AndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork() // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .penaltyFlashScreen()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AndroidApplication)
            modules(
                networkModule,
                providersModule,
                interactorsModule,
                viewModelModule
            )
        }
    }
}
