package woowacourse.shopping.di

import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.util.autoDI.autoDIModule.autoDIModule

val viewModelModule = autoDIModule {
    viewModel { MainViewModel(inject(), inject()) }
}
