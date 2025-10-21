package woowacourse.bibi.di.androidx

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.bibi.di.core.Container

inline fun <reified T : ViewModel> ComponentActivity.injectedViewModel(crossinline appContainerProvider: () -> Container): T {
    val container = appContainerProvider()
    return ViewModelProvider(this, InjectingViewModelFactory(container))[T::class.java]
}
