package woowacourse.shopping.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ShoppingApplication
import kotlin.reflect.KClass

fun <T : ViewModel> ComponentActivity.injectedViewModel(vm: KClass<T>): T {
    val container = (application as ShoppingApplication).container
    return ViewModelProvider(this, InjectingViewModelFactory(container))[vm.java]
}
