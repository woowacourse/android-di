package woowacourse.shopping.di

import com.daedan.di.DependencyModule
import com.daedan.di.DiApplication
import com.daedan.di.module
import com.daedan.di.util.annotated
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

fun DiApplication.viewModelModule(): DependencyModule =
    module {
        viewModel { CartViewModel(get(annotated<RoomDBCartRepository>())) }
        viewModel { MainViewModel() }
    }
