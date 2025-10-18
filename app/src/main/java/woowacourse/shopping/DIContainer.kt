package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass

object DIContainer {
    private val providers = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>>, () -> Any>()

    fun register(type: KClass<*>, qualifier: KClass<out Annotation>, provider: () -> Any) {
        providers[type to qualifier] = provider
    }

    private val cache = mutableMapOf<KClass<*>, Any>()

    fun get(type: KClass<*>, qualifier: KClass<out Annotation>): Any {
        return cache.getOrPut(type) {
            providers[type to qualifier]?.invoke() ?: throw IllegalArgumentException("No provider for $type")
        }
    }

    fun registerDependencies(appContext: Context) {
        register(ProductRepository::class, Remote::class) { DefaultProductRepository() }

        register(CartRepository::class, Remote::class) {
            val db = ShoppingDatabase.getInstance(appContext)
            DefaultCartRepository(db.cartProductDao())
        }
    }
}
