package woowacourse.shopping.di

import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase

class RoomDBModule : Module {
    override fun provideInstance(dependencyRegistry: DependencyRegistry) {
        dependencyRegistry.addInstance(
            CartProductDao::class,
            ShoppingDatabase.instanceOrNull.cartProductDao(),
        )
    }
}
