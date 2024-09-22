package woowacourse.shopping

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.DependencyInjector

class BaseViewModelFactory(
    appContainer: AppContainer,
) : ViewModelProvider.Factory {
    private val dependencyInjector = DependencyInjector(appContainer)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return dependencyInjector.createInstance(modelClass)
    }
}

inline fun <reified VM : ViewModel> injectedViewModelFactory(): ViewModelProvider.Factory =
    viewModelFactory {
        initializer {
            val appContainer = (this[APPLICATION_KEY] as ShoppingApplication).container
            BaseViewModelFactory(appContainer).create(VM::class.java)
        }
    }

inline fun <reified VM : ViewModel> ComponentActivity.injectedSh1mj1ViewModel(): Lazy<VM> =
    viewModels<VM> {
        injectedViewModelFactory<VM>()
    }
