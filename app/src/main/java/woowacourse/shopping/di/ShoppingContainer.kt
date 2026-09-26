package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.LocalMemoryCart
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass

class ShoppingContainer(
    context: Context,
) {
    private val instances = mutableMapOf<BindingKey, Any>()
    private val factories = mutableMapOf<BindingKey, () -> Any>()

    private val database =
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                "shopping.db",
            ).build()

    init {
        factories[BindingKey(CartProductDao::class, null)] = {
            database.cartProductDao()
        }

        factories[BindingKey(CartRepository::class, LocalMemoryCart::class)] = {
            val dao = getInstance(CartProductDao::class) as CartProductDao
            DefaultCartRepository(dao)
        }

        factories[BindingKey(CartRepository::class, InMemoryCart::class)] = {
            InMemoryCartRepository()
        }
    }

    fun getInstance(
        targetClass: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
    ): Any? {
        val key =
            if (qualifier != null) {
                BindingKey(targetClass, qualifier)
            } else {
                val candidates = (instances.keys + factories.keys).filter { it.type == targetClass }.distinct()
                if (candidates.size > 1) {
                    val names = candidates.map { it.qualifier?.simpleName ?: "unqualified" }.sorted()
                    throw IllegalArgumentException(
                        "[Qualifier 지정 필요] ${targetClass.simpleName}에 여러 구현체가 등록되어 있습니다: $names.",
                    )
                }
                candidates.singleOrNull() ?: return null
            }

        instances[key]?.let { return it }

        val factory = factories[key] ?: return null
        val instance = factory()

        instances[key] = instance

        return instance
    }

    fun saveInstance(
        key: KClass<*>,
        value: Any,
        qualifier: KClass<out Annotation>? = null,
    ) {
        instances[BindingKey(key, qualifier)] = value
    }
}
