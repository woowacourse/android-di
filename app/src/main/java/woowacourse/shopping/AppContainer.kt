package woowacourse.shopping

import woowacourse.di.InjectedComponent
import kotlin.reflect.KClass

abstract class AppContainer {
    abstract fun add(component: InjectedComponent)

    abstract fun add(vararg component: InjectedComponent)

    abstract fun find(clazz: KClass<*>): Any?

    abstract fun clearActivityScopedObjects()
}
