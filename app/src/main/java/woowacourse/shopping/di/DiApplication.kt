package woowacourse.shopping.di

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.DiContainer

open class DiApplication : Application() {
    private val diApplicationModule: DiApplicationModule
        by lazy { DiApplicationModule(applicationContext) }

    private val diContainer: DiContainer
        by lazy { DiContainer(diApplicationModule) }

    override fun onCreate() {
        super.onCreate()
        diContainer.inject(this)
    }

    fun getActivityDiContainer(activity: ComponentActivity): DiViewModel {
        return ViewModelProvider(activity)[DiViewModel::class.java].apply {
            if (isInitialized.not()) {
                ownerRetainedDiContainer = DiRetainedActivityModule(diApplicationModule, activity)
                viewModelDiContainer = DiViewModelModule(ownerRetainedDiContainer)
            }
            ownerDiContainer = DiActivityModule(ownerRetainedDiContainer)
        }
    }
}
