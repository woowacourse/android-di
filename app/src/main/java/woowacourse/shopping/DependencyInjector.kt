package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import java.lang.reflect.Constructor

object DependencyInjector {
    private val instances = mutableMapOf<Class<*>, Any>()

    private const val CONSTRUCTOR_NOT_FOUND = "적합한 생성자를 찾을 수 없습니다."
    private const val MINIMUM_PARAMETER_COUNT = 0

    fun initialize(context: Context) {
        val cartProductDao = ShoppingDatabase.initialize(context).cartProductDao()
        instances[ProductRepository::class.java] = RepositoryModule.provideProductRepository()
        instances[CartRepository::class.java] = RepositoryModule.provideCartRepository(cartProductDao)
    }

    fun <T> getInstance(clazz: Class<T>): T {
        instances[clazz]?.let { instance -> return instance as T }

        val instance = createInstance(clazz)
        instances[clazz] = instance as Any

        return instance
    }

    private fun <T> createInstance(clazz: Class<T>): T {
        val constructor: Constructor<*> =
            clazz.declaredConstructors.firstOrNull { it.parameterCount > MINIMUM_PARAMETER_COUNT }
                ?: throw IllegalArgumentException(CONSTRUCTOR_NOT_FOUND)

        constructor.isAccessible = true

        val dependencies =
            constructor.parameterTypes.map { paramClass ->
                getInstance(paramClass)
            }

        return constructor.newInstance(*dependencies.toTypedArray()) as T
    }
}
