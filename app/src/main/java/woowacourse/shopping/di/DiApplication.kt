package woowacourse.shopping.di

import android.app.Application
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.re4rk.arkdi.DiContainer

open class DiApplication : Application() {
    private val diApplicationModule: DiApplicationModule by lazy {
        DiApplicationModule(applicationContext)
    }

    private val diContainer: DiContainer by lazy {
        DiContainer(diApplicationModule)
    }

    override fun onCreate() {
        super.onCreate()
        diContainer.inject(this)
    }

    fun getActivityDiContainer(activity: ComponentActivity): DiViewModel {
        return ViewModelProvider(activity)[DiViewModel::class.java].apply {
            val retainedActivityModule = DiRetainedActivityModule(activity)
            val activityModule = DiActivityModule()
            val viewModelModule = DiViewModelModule()

            if (ownerRetainedDiContainer == null) {
                ownerRetainedDiContainer = DiContainer(diApplicationModule, retainedActivityModule)
            }

            ownerDiContainer =
                DiContainer(diApplicationModule, retainedActivityModule, activityModule)

            if (viewModelDiContainer == null) {
                viewModelDiContainer =
                    DiContainer(diApplicationModule, retainedActivityModule, viewModelModule)
            }
        }
    }
}
