package woowacourse.shopping.di

import woowacourse.shopping.ui.MainViewModel
import com.angrypig.autodi.autoDIModule.autoDIModule

val viewModelModule = com.angrypig.autodi.autoDIModule.autoDIModule {
    viewModel { MainViewModel(inject(), inject()) }
}
