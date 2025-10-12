package woowacourse.shopping.di.module

import woowacourse.shopping.di.Provider
import woowacourse.shopping.di.definition.DefinitionInformation
import woowacourse.shopping.di.definition.Kind
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

class ViewModelModule : InjectionModule {
    override fun provideDefinitions(): List<DefinitionInformation<*>> =
        listOf(
            DefinitionInformation(
                kclass = MainViewModel::class,
                qualifier = null,
                kind = Kind.FACTORY,
                provider = { injector ->
                    Provider { MainViewModel(injector.get(), injector.get()) }
                },
            ),
            DefinitionInformation(
                kclass = CartViewModel::class,
                qualifier = null,
                kind = Kind.FACTORY,
                provider = { injector -> Provider { CartViewModel(injector.get()) } },
            ),
        )
}
