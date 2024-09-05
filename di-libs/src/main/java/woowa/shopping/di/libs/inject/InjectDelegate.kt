package woowa.shopping.di.libs.inject

import woowa.shopping.di.libs.container.Containers
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class InjectDelegate<T : Any>(private val clazz: KClass<T>) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return Containers.get(clazz)
    }
}

inline fun <reified T : Any> inject(): InjectDelegate<T> {
    return InjectDelegate(T::class)
}