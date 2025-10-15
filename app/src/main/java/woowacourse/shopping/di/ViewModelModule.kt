package woowacourse.shopping.di

import com.daedan.compactAndroidDi.DependencyModule
import com.daedan.compactAndroidDi.DiApplication
import com.daedan.compactAndroidDi.module
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

fun DiApplication.viewModelModule(): DependencyModule =
    module {
        viewModel { CartViewModel(get(annotated<RoomDBCartRepository>())) }
        viewModel { MainViewModel() }
    }
