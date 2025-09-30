package woowacourse.shopping.di

import android.app.Application
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

class AppContainerDelegate<T>(
    private val appContainer: AppContainer,
) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T = appContainer.instantiate(property.returnType.jvmErasure) as T
}

inline fun <reified T> Application.containerProvider(): AppContainerDelegate<T> {
    val store = (this as MainApplication).appContainer
    return AppContainerDelegate(store)
}
