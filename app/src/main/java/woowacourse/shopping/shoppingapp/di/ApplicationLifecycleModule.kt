package woowacourse.shopping.shoppingapp.di

import com.woowacourse.di.DiContainer
import com.woowacourse.di.DiModule
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl

class ApplicationLifecycleModule : DiModule {
    fun bindCartRepository(diContainer: DiContainer) {
        diContainer.bind(
            CartRepository::class,
            CartRepositoryImpl::class,
        )
    }
}
