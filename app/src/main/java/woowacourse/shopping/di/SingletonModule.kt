package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository

class SingletonModule {
    private val cacheData: MutableMap<String, Any> = mutableMapOf()

    fun provideCartRepository(): CartRepository {
        return getInstance { DefaultCartRepository() }
    }

    fun provideProductRepository(): ProductRepository {
        return getInstance { DefaultProductRepository() }
    }

    private inline fun <reified T : Any> getInstance(create: () -> T): T {
        val qualifiedName = T::class.qualifiedName ?: throw IllegalArgumentException()
        return cacheData[qualifiedName] as T? ?: run {
            create().also { cacheData[qualifiedName] = it }
        }
    }
}
