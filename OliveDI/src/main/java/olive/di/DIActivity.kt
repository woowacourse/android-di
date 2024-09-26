package olive.di

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy

abstract class DIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(DIActivityLifecycleTracker())
        DIContainer.injectFieldDependency(this)
    }

    inline fun <reified VM : ViewModel> injectViewModel(): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            { ViewModelFactory() },
            { this.defaultViewModelCreationExtras }
        )
    }
}
