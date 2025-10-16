package woowacourse.shopping.data

import com.example.di.Dependency
import com.example.di.Module
import woowacourse.shopping.domain.CartRepository

class FakeRepositoryModule : Module {
    @Dependency
    @DatabaseRepository
    val databaseCartRepository: CartRepository by lazy { DatabaseCartRepository(FakeCartProductDao()) }
}
