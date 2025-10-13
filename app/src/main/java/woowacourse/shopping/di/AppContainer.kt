package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

data class DIKey(
    val kClass: KClass<*>,
    val qualifierClass: KClass<*>? = null,
)

class AppContainer(
    context: Context,
) {
    private val providers = mutableMapOf<DIKey, () -> Any>()
    private val instances = mutableMapOf<DIKey, Any>()

    init {
        val database =
            Room
                .inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        providers[DIKey(CartRepository::class, RoomDatabase::class)] =
            { DefaultCartRepository(database.cartProductDao()) }

        providers[DIKey(ProductRepository::class, InMemory::class)] = { DefaultProductRepository() }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(
        clazz: KClass<T>,
        qualifierClass: KClass<*>? = null,
    ): T {
        val key = DIKey(clazz, qualifierClass)
        instances[key]?.let { return it as T }

        val factory =
            providers[key]
                ?: throw IllegalArgumentException(
                    "${clazz.simpleName} provider(${qualifierClass?.simpleName ?: "default"}) 없음. DIKey: $key",
                )

        // 인스턴스 생성 및 캐싱
        val instance = factory() as T
        instances[key] = instance
        return instance
    }
}
