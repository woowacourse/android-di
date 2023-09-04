package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl

class SingletonModule {
    private val cacheData: MutableMap<String, Any> = mutableMapOf()

    fun provideCartRepository(): CartRepository {
        return getInstance { CartRepositoryImpl() }
    }

    fun provideProductRepository(): ProductRepository {
        return getInstance { ProductRepositoryImpl() }
    }

    private inline fun <reified T : Any> getInstance(create: () -> T): T {
        val qualifiedName = T::class.qualifiedName ?: throw IllegalArgumentException()
        return cacheData[qualifiedName] as T? ?: run {
            create().also { cacheData[qualifiedName] = it }
        }
    }
}
