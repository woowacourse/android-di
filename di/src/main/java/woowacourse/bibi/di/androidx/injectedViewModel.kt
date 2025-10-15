package woowacourse.bibi.di.androidx

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.bibi.di.core.AppContainer

inline fun <reified T : ViewModel> ComponentActivity.injectedViewModel(crossinline containerProvider: () -> AppContainer): T {
    val container = containerProvider()
    return ViewModelProvider(this, InjectingViewModelFactory(container))[T::class.java]
}
