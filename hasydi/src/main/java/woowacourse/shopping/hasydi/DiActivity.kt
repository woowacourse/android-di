package woowacourse.shopping.hasydi

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

abstract class DiActivity(private val modules: List<Module> = listOf()) : AppCompatActivity() {

    private val tag = this::class.simpleName.toString()

    private lateinit var injector: Injector

    val viewModelFactory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return injector.inject(modelClass.kotlin)
        }
    }

    @MainThread
    inline fun <reified VM : ViewModel> viewModels(): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            { viewModelFactory },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector = (application as DiApplication).injector

        setupLifecycleTracker()
        setupActivityContainer()

        injector.fieldInjection(this::class, this)
    }

    private fun setupActivityContainer() {
        if (!injector.hasContainer(tag)) {
            val activityContainer = AppContainer(modules, this)
            injector.setActivityContainer(tag, activityContainer)
        }
    }

    private fun setupLifecycleTracker() {
        val activityLifecycleTracker = ActivityLifecycleTracker()
        lifecycle.addObserver(activityLifecycleTracker)
    }
}
