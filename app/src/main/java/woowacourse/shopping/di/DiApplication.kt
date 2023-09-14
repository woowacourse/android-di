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
            val activityModule = DiActivityModule(activity)
            val viewModelModule = DiViewModelModule()
            ownerDiContainer = DiContainer(diApplicationModule, activityModule)
            viewModelDiContainer = DiContainer(diApplicationModule, activityModule, viewModelModule)
        }
    }
}
