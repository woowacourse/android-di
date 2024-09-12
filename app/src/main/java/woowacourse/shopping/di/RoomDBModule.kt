package woowacourse.shopping.di

import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase
import kotlin.reflect.KClass

class RoomDBModule : Module {
    private val dependencies = mutableMapOf<KClass<*>, KClass<*>>()

    override fun provideInstance(dependencyRegistry: DependencyRegistry) {
        dependencyRegistry.addInstance(
            CartProductDao::class,
            ShoppingDatabase.instanceOrNull.cartProductDao(),
        )
    }

    override fun findQualifierOrNull(classType: KClass<*>): KClass<*>? = dependencies[classType]
}
