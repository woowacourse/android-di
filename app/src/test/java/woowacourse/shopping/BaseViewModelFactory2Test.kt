package woowacourse.shopping

import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.FakeInjectedActivityContainer
import woowacourse.shopping.di.FakeInjectedSingletonContainer
import woowacourse.shopping.di.InjectedActivityContainer
import woowacourse.shopping.di.InjectedComponent
import woowacourse.shopping.di.InjectedSingletonContainer
import woowacourse.shopping.ui.MainViewModel

class BaseViewModelFactory2Test {
    private lateinit var injectedSingletonContainer: InjectedSingletonContainer
    private lateinit var injectedActivityContainer: InjectedActivityContainer
    private lateinit var appContainer: AppContainer

    @Test
    fun baseViewModel_create_does_not_throw_error() {
        // given
        injectedSingletonContainer =
            FakeInjectedSingletonContainer(
                components =
                    mutableListOf(
                        InjectedComponent.InjectedSingletonComponent(
                            ProductRepository::class,
                            DefaultProductRepository(),
                        ),
                        InjectedComponent.InjectedSingletonComponent(
                            CartRepository::class,
                            FakeCartRepository(),
                        ),
                    ),
            )
        injectedActivityContainer = FakeInjectedActivityContainer()
        appContainer = DefaultAppContainer(injectedSingletonContainer, injectedActivityContainer)
        val baseViewModelFactory = BaseViewModelFactory2(appContainer)

        assertDoesNotThrow { (baseViewModelFactory.create(MainViewModel::class.java)) }
    }
}
