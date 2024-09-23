package woowacourse.shopping.data.di

import com.android.di.component.DiContainer
import com.android.di.component.Module
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.domain.CartRepository

class ApplicationLifeModule: Module {
    fun bindCartRepository(diContainer: DiContainer) {
        diContainer.bind(
            CartRepository::class,
            CartRepositoryImpl::class,
        )
    }
}
