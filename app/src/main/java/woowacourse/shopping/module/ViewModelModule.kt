package woowacourse.shopping.module

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import woowacourse.shopping.presentation.MainViewModel
import woowacourse.shopping.presentation.cart.CartViewModel


val viewModelModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::CartViewModel)
}