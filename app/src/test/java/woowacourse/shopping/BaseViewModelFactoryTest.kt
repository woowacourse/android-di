package woowacourse.shopping

import com.example.sh1mj1.annotation.Qualifier
import com.example.sh1mj1.component.InjectedComponent.InjectedSingletonComponent
import com.example.sh1mj1.component.InjectedSingletonContainer
import com.example.sh1mj1.container.AppContainer
import com.example.sh1mj1.container.InjectedActivityContainer
import org.junit.Test
import org.junit.jupiter.api.assertDoesNotThrow
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.FakeInjectedActivityContainer
import woowacourse.shopping.di.FakeInjectedSingletonContainer
import woowacourse.shopping.ui.MainViewModel

class BaseViewModelFactoryTest {
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
                        InjectedSingletonComponent(
                            injectedClass = ProductRepository::class,
                            instance = InMemoryProductRepository(),
                            qualifier = Qualifier("InMemory"),
                        ),
                        InjectedSingletonComponent(
                            injectedClass = CartRepository::class,
                            instance = FakeCartRepository(),
                            qualifier = Qualifier("RoomDao"),
                        ),
                    ),
            )
        injectedActivityContainer = FakeInjectedActivityContainer()
        appContainer = DefaultAppContainer(injectedSingletonContainer, injectedActivityContainer)
        val baseViewModelFactory = BaseViewModelFactory(appContainer)

        assertDoesNotThrow { (baseViewModelFactory.create(MainViewModel::class.java)) }
    }
}
