package woowacourse.shopping

import android.content.Context
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass

//private val defaultProductRepository by lazy { DefaultProductRepository() }
//private val defaultCartRepository by lazy { DefaultCartRepository() }

//object DIContainer {
//    private val dependencies by lazy {
//        mapOf<KClass<*>, Any>(
//            ProductRepository::class to defaultProductRepository,
//            CartRepository::class to defaultCartRepository,
//        )
//    }
//
//    fun get(type: KClass<*>): Any = dependencies[type] ?: throw IllegalArgumentException("No dependency for $type")
//}

object DIContainer {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()

    fun register(type: KClass<*>, provider: () -> Any) {
        providers[type] = provider
    }

    private val cache = mutableMapOf<KClass<*>, Any>()

    fun get(type: KClass<*>): Any {
        return cache.getOrPut(type) {
            providers[type]?.invoke() ?: throw IllegalArgumentException("No provider for $type")
        }
    }

    fun registerDependencies(appContext: Context) {
        DIContainer.register(ProductRepository::class) { DefaultProductRepository() }

        DIContainer.register(CartRepository::class) {
            // 여기서 getInstance 호출 -> 최초 get() 시점에 실행
            val db = ShoppingDatabase.getInstance(appContext)
            DefaultCartRepository(db.cartProductDao())
        }
    }
}
