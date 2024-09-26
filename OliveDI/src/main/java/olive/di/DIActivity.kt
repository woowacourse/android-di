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
        addActivityScopeInstance()
        DIContainer.injectFieldDependency(this)
    }

    private fun addActivityScopeInstance() {
        activityInstances.forEach { (type, instanceProvider) ->
            val instance = instanceProvider.get()
            instances[type] = instance
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeActivityScopeInstance()
    }

    private fun removeActivityScopeInstance() {
        activityInstances.forEach { (type, _) ->
            instances.remove(type)
        }
    }
}

inline fun <reified VM : ViewModel> DIActivity.injectViewModel(): Lazy<VM> {
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
