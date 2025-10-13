package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartDatabaseRepository
import woowacourse.shopping.data.repository.CartInMemoryRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.di.annotation.RepositoryType
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass

class AppContainerImpl(
    context: Context,
) : AppContainer {
    private val dependencies = mutableMapOf<DependencyKey, Any>()
    private val database: ShoppingDatabase by lazy { ShoppingDatabase.getDatabase(context) }

    init {
        register(ProductRepository::class, ProductDefaultRepository())
        register(
            CartRepository::class,
            CartDatabaseRepository(database.cartProductDao()),
            RepositoryType.ROOM_DB,
        )
        register(
            CartRepository::class,
            CartInMemoryRepository(),
            RepositoryType.IN_MEMORY,
        )
    }

    override fun <T : Any> register(
        kClass: KClass<T>,
        instance: T,
        qualifier: String?,
    ) {
        val key = DependencyKey(kClass, qualifier)
        dependencies[key] = instance
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(
        kClass: KClass<T>,
        qualifier: String?,
    ): T {
        val key = DependencyKey(kClass, qualifier)
        return dependencies[key] as? T
            ?: throw IllegalArgumentException("${kClass.simpleName} 타입의 인스턴스가 등록되어 있지 않습니다.")
    }

    override fun <T : Any> canResolve(
        klass: KClass<T>,
        qualifier: String?,
    ): Boolean {
        val key = DependencyKey(klass, qualifier)
        return dependencies.containsKey(key)
    }
}
