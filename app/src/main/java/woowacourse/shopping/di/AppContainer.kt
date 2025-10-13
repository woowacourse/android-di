package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

object AppContainer {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()
    private val instances = ConcurrentHashMap<KClass<*>, Any>()

    private val implementationMap = mutableMapOf<KClass<*>, KClass<*>>()

    fun init(context: Context) {
        registerImplementation(CartRepository::class, CartRepositoryImpl::class)
        registerImplementation(ProductRepository::class, ProductRepositoryImpl::class)
        registerProvider(ShoppingDatabase::class) {
            Room
                .databaseBuilder(
                    context.applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping-db",
                ).build()
        }
        registerProvider(CartProductDao::class) {
            get(ShoppingDatabase::class).cartProductDao()
        }
    }

    private fun <T : Any> registerImplementation(
        interfaceClass: KClass<T>,
        implementationClass: KClass<out T>,
    ) {
        implementationMap[interfaceClass] = implementationClass
    }

    private fun <T : Any> registerProvider(
        type: KClass<T>,
        provider: () -> T,
    ) {
        providers[type] = provider
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(serviceClass: KClass<T>): T {
        if (instances.containsKey(serviceClass)) {
            return instances[serviceClass] as T
        }

        val provider = providers[serviceClass]
        val instance =
            if (provider != null) {
                provider()
            } else {
                val concreteClass = implementationMap[serviceClass] ?: serviceClass
                createInstance(concreteClass)
            }

        instances[serviceClass] = instance
        return instance as T
    }

    fun <T : Any> createInstance(concreteClass: KClass<T>): T {
        val constructor =
            concreteClass.constructors.first()
        val args =
            constructor.parameters
                .filter { parameter -> parameter.hasAnnotation<Inject>() }
                .map { parameter ->
                    val parameterType = parameter.type.classifier as KClass<*>
                    get(parameterType)
                }.toTypedArray()

        @Suppress("UNCHECKED_CAST")
        return constructor.call(*args)
    }
}
