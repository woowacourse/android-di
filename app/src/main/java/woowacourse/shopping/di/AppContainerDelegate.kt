package woowacourse.shopping.di

import android.app.Application
import woowacourse.shopping.ui.MainApplication
import kotlin.reflect.KProperty
import kotlin.reflect.jvm.jvmErasure

class AppContainerDelegate<T>(
    private val appContainerStore: AppContainerStore,
) {
    @Suppress("UNCHECKED_CAST")
    operator fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T = appContainerStore.instantiate(property.returnType.jvmErasure) as T
}

inline fun <reified T> Application.containerProvider(): AppContainerDelegate<T> {
    val store = (this as MainApplication).appContainerStore
    return AppContainerDelegate(store)
}
