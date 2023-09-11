package woowacourse.shopping.di

import com.angrypig.autodi.autoDIModule.autoDIModule
import woowacourse.shopping.ui.MainViewModel

val viewModelModule = autoDIModule {
    viewModel { MainViewModel(inject(), inject()) }
}
