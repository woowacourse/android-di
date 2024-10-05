package woowacourse.shopping.module

import org.koin.dsl.module
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.LocalCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.presentation.MainViewModel

val singletonRepositoryModule = module {
    single<CartRepository> { LocalCartRepository(get()) }
}

val viewModelScopeRepositoryModule = module {
    scope<MainViewModel> {
        scoped<ProductRepository> { InMemoryProductRepository() }
    }
}