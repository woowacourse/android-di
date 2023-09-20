package woowacourse.shopping.di

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

open class DIActivity : AppCompatActivity() {
    private lateinit var activityModule: ActivityModule

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModule = (application as DIApplication).getActivityModule()
        activityModule.inject(this)
    }

    inline fun <reified VM : ViewModel> ComponentActivity.injectViewModel(): Lazy<VM> {
        return ViewModelLazy(
            VM::class,
            { viewModelStore },
            {
                viewModelFactory {
                    initializer {
                        (application as DIApplication).getViewModelModule()
                            .createViewModel(VM::class.java)
                    }
                }
            },
        )
    }
}
