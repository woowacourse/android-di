package woowacourse.shopping.di

import com.example.di.DiContainer
import com.example.di.Module
import woowacourse.shopping.data.local.CartProductDao
import woowacourse.shopping.data.local.ShoppingDatabase

class RoomDBModule : Module {
    override fun provideInstance(dependencyRegistry: DiContainer) {
        DiContainer.addInstance(
            CartProductDao::class,
            ShoppingDatabase.instanceOrNull.cartProductDao(),
        )
    }
}
