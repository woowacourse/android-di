package olive.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

abstract class DIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(DIActivityLifecycleTracker())
        DIContainer.injectFieldDependency(this)
    }

    inline fun <reified VM : ViewModel> injectViewModel(): Lazy<VM> {
        val viewModelFactory = {
            viewModelFactory {
                initializer {
                    DIContainer.instance(VM::class)
                }
            }
        }

        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            viewModelFactory,
            { this.defaultViewModelCreationExtras }
        )
    }
}
