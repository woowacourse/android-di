package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

typealias instance = Any

object DependencyInjector {
    private val instances = mutableMapOf<KClass<*>, instance>()

    private const val CONSTRUCTOR_NOT_FOUND = "적합한 생성자를 찾을 수 없습니다."
    private const val DEPENDENCY_TYPE_IS_INVALID = "의존성 클래스 타입이 올바르지 않습니다."

    fun initialize(context: Context) {
        val cartProductDao = ShoppingDatabase.initialize(context).cartProductDao()
        addInstance(ProductRepository::class, RepositoryModule.provideProductRepository())
        addInstance(CartRepository::class, RepositoryModule.provideCartRepository(cartProductDao))
    }

    fun <T : Any> findInstance(clazz: KClass<T>): T = instances[clazz] as? T ?: createInstance(clazz)

    private fun <T : Any> addInstance(
        clazz: KClass<T>,
        instance: T,
    ) {
        instances[clazz] = instance
    }

    private fun <T : Any> createInstance(clazz: KClass<T>): T {
        val constructor: KFunction<T> = getPrimaryConstructor(clazz)
        val dependencies: List<Any?> = constructor.extractDependencies()
        val instance = constructor.call(*dependencies.toTypedArray())

        injectFields(clazz, instance)

        return instance
    }

    private fun <T : Any> getPrimaryConstructor(clazz: KClass<T>): KFunction<T> =
        clazz.primaryConstructor ?: throw IllegalArgumentException(CONSTRUCTOR_NOT_FOUND)

    private fun <T : Any> KFunction<T>.extractDependencies(): List<Any?> =
        parameters.map { parameter ->
            when (val classifier = parameter.type.classifier) {
                is KClass<*> -> findInstance(classifier)
                else -> throw IllegalArgumentException(DEPENDENCY_TYPE_IS_INVALID)
            }
        }

    private fun <T : Any> injectFields(
        clazz: KClass<T>,
        instance: T,
    ) {
        clazz.declaredMemberProperties.forEach { kProperty ->
            if (kProperty.hasAnnotation<Inject>()) {
                val classifier: KClass<*> = kProperty.returnType.classifier as KClass<*>
                val dependency = findInstance(classifier)

                kProperty as KMutableProperty1
                kProperty.setter.call(instance, dependency)
            }
        }
    }
}
