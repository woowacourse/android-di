package woowacourse.shopping.di

import kotlin.reflect.KClass

object DiContainer {
    private const val ERROR_MESSAGE = "%s 인스턴스가 생성되지 않았습니다"

    private val instances: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T : Any> postInstance(
        kClass: KClass<T>,
        instance: Any,
    ) {
        instances[kClass] = instance
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(kClass: KClass<T>): T =
        instances[kClass] as? T
            ?: throw IllegalArgumentException(ERROR_MESSAGE.format(kClass.simpleName))
}
