package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase

class SingletonModule(
    context: Context,
) : Module {
    private val cartProductDao: CartProductDao =
        ShoppingDatabase.getShoppingDatabase(context).cartProductDao()
    private val cacheData: MutableMap<String, Any> = mutableMapOf()

    fun provideCartRepository(cartProductDao: CartProductDao): CartRepository {
        return getInstance { RoomCartRepository(cartProductDao) }
    }

    fun provideInMemoryCartRepository(): CartRepository {
        return getInstance { InMemoryCartRepository() }
    }

    fun provideProductRepository(): ProductRepository {
        return getInstance { DefaultProductRepository() }
    }

    fun provideCartProductDao(): CartProductDao = cartProductDao

    private inline fun <reified T : Any> getInstance(create: () -> T): T {
        val qualifiedName = T::class.qualifiedName ?: throw IllegalArgumentException()
        return cacheData[qualifiedName] as T? ?: run {
            create().also { cacheData[qualifiedName] = it }
        }
    }

    companion object {
        private const val CART_PRODUCT_DATABASE = "CART_PRODUCT_DATABASE"
        private var instance: SingletonModule? = null

        fun getSingletonModule(
            context: Context,
        ): SingletonModule {
            synchronized(this) {
                instance?.let { return it }
                return SingletonModule(context).also { instance = it }
            }
        }
    }
}
