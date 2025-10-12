package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

class AppContainer(
    context: Context,
) {
    private val providers = mutableMapOf<KClass<*>, () -> Any>()

    init {
        val database =
            Room
                .inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java)
                .allowMainThreadQueries()
                .build()

        providers[CartRepository::class] = { DefaultCartRepository(database.cartProductDao()) }
        providers[ProductRepository::class] = { DefaultProductRepository() }
    }

    private val instances = mutableMapOf<KClass<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: KClass<T>): T {
        instances[clazz]?.let { return it as T }

        val factory =
            providers[clazz]
                ?: throw IllegalArgumentException("${clazz.simpleName} provider 없음")

        // 인스턴스 생성 및 캐싱
        val instance = factory() as T
        instances[clazz] = instance
        return instance
    }
}
