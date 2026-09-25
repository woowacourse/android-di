package woowacourse.shopping.data.di

import io.github.firstwoosun.di.InstanceProvider
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass

class DataContainer(
    private val providers: Map<KClass<*>, () -> Any>,
) : InstanceProvider {
    private val instances = mutableMapOf<KClass<*>, Any>()

    override fun getInstanceOrNull(type: KClass<*>): Any? {
        if (instances[type] != null) return instances[type]

        val provider = providers[type] ?: return null
        val instance = provider()

        instances[type] = instance
        return instance
    }

    companion object {
        fun create(database: ShoppingDatabase): DataContainer {
            val providers =
                mapOf<KClass<*>, () -> Any>(
                    ShoppingDatabase::class to { database },
                    CartProductDao::class to { database.cartProductDao() },
                )
            return DataContainer(providers)
        }

        fun testInstanceCreate(vararg providers: Pair<KClass<*>, () -> Any>) = DataContainer(providers.toMap())
    }
}
