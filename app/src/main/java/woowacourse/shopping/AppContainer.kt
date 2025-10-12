package woowacourse.shopping

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import kotlin.reflect.KClass

class AppContainer(
    private val context: Context,
) {
    private val database: ShoppingDatabase by lazy {
        Room
            .databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                "shopping-database",
            ).build()
    }
    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        providers[CartRepository::class] =
            DefaultCartRepository(dao = database.cartProductDao())
        providers[ProductRepository::class] = DefaultProductRepository()
    }

    fun <T : Any> get(clazz: KClass<T>): T =
        providers[clazz] as? T
            ?: throw IllegalArgumentException("${clazz.simpleName} provider를 찾을 수 없습니다")
}
