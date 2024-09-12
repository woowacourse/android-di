package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.local.LocalCartRepository
import woowacourse.shopping.data.local.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KClass

class RepositoryModule : Module {
    private val dependencies = mutableMapOf<KClass<*>, KClass<*>>()

    fun addDependencies(
        classType: KClass<*>,
        implType: KClass<*>,
    ) {
        dependencies[classType] = implType
    }

    override fun provideInstance(registry: DependencyRegistry) {
        registry.addInstance(ProductRepository::class, DefaultProductRepository())
        registry.addInstance(
            CartRepository::class,
            LocalCartRepository(ShoppingDatabase.instanceOrNull.cartProductDao()),
        )
    }

    override fun findQualifierOrNull(classType: KClass<*>): KClass<*>? = dependencies[classType]
}
