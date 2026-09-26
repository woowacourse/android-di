package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass

class ShoppingContainer(
    context: Context,
) {
    private val instances = mutableMapOf<KClass<*>, Any>()
    private val factories = mutableMapOf<KClass<*>, () -> Any>()

    private val database =
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()

    init {
        factories[CartProductDao::class] = {
            database.cartProductDao()
        }

        factories[CartRepository::class] = {
            val dao = getInstance(CartProductDao::class) as CartProductDao
            DefaultCartRepository(dao)
        }
    }

    fun getInstance(targetClass: KClass<*>): Any? {
        instances[targetClass]?.let { return it }

        val factory = factories[targetClass] ?: return null
        val instance = factory()

        instances[targetClass] = instance

        return instance
    }

    fun saveInstance(
        key: KClass<*>,
        value: Any,
    ) {
        instances[key] = value
    }
}
