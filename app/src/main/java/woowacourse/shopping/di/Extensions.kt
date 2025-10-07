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

fun <T, R> MutableSet<T>.withElement(
    element: T,
    block: () -> R,
): R {
    if (!add(element)) {
        error("순환 의존성 감지: $element\n현재 체인: $this")
    }
    return try {
        block()
    } finally {
        remove(element)
    }
}
