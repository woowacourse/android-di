package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

typealias instance = Any

object DependencyInjector {
    private val instances = mutableMapOf<KClass<*>, instance>()

    private const val CONSTRUCTOR_NOT_FOUND = "적합한 생성자를 찾을 수 없습니다."
    private const val DEPENDENCY_TYPE_IS_INVALID = "의존성 클래스 타입이 올바르지 않습니다."

    fun initialize(context: Context) {
        val cartProductDao = ShoppingDatabase.initialize(context).cartProductDao()
        instances[ProductRepository::class] = RepositoryModule.provideProductRepository()
        instances[CartRepository::class] = RepositoryModule.provideCartRepository(cartProductDao)
    }

    fun <T : Any> getInstance(clazz: Class<T>): T {
        val kClass: KClass<T> = clazz.kotlin

        val instance: T = createInstance(kClass)
        instances[kClass] = instance

        return instance
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor: KFunction<T> = getPrimaryConstructor(clazz)
        val dependencies: List<Any?> = constructor.extractDependencies()

        return constructor.call(*dependencies.toTypedArray())
    }

    private fun <T : Any> getPrimaryConstructor(clazz: KClass<T>): KFunction<T> =
        clazz.primaryConstructor ?: throw IllegalArgumentException(CONSTRUCTOR_NOT_FOUND)

    private fun <T : Any> KFunction<T>.extractDependencies(): List<Any?> =
        parameters.map { parameter ->
            when (val classifier = parameter.type.classifier) {
                is KClass<*> -> instances[classifier]
                else -> throw IllegalArgumentException(DEPENDENCY_TYPE_IS_INVALID)
            }
        }
}
