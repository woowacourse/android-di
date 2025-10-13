package woowacourse.shopping.di

import com.shopping.di.InjectContainer
import com.shopping.di.InjectionModule
import com.shopping.di.Provider
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ViewModelModule : InjectionModule {
    override fun provideDefinitions(container: InjectContainer) {
        container.apply {
            registerFactory<MainViewModel> { Provider { MainViewModel() } }
            registerFactory<CartViewModel> { Provider { CartViewModel() } }
        }
    }
}
