package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

object RepositoryProvider {
    private val instances = mutableMapOf<KClass<*>, Any>()

    init {
        registerModule(woowacourse.shopping.data.repository.RepositoryModule)
    }

    private fun registerModule(module: Any) {
        module::class.memberProperties.forEach { property ->
            val instance = property.getter.call(module) ?: return@forEach

            val kClass = property.returnType.classifier as? KClass<*> ?: return@forEach
            instances[kClass] = instance
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(kClass: KClass<T>): T =
        instances[kClass] as? T
            ?: throw IllegalStateException("Dependency not found for ${kClass.simpleName}")

    inline fun <reified T : Any> get(): T = get(T::class)
}
