package woowacourse.shopping

import kotlin.reflect.KClass

class ShoppingContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val makers = mutableMapOf<KClass<*>, () -> Any>()

    fun <T : Any> make(
        type: KClass<T>,
        make: () -> T,
    ) {
        makers[type] = make
    }

    fun <T : Any> get(type: KClass<T>): T {
        instances[type]?.let { return it as T }

        val maker: () -> Any = makers[type] ?: error("${type.simpleName} 만드는 방법이 없습니다.")
        val newInstance: Any = maker()
        instances[type] = newInstance
        return newInstance as T
    }
}
