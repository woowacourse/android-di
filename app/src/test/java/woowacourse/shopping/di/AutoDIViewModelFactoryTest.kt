package woowacourse.shopping.di

import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import kotlin.reflect.KClass

class AutoDIViewModelFactoryTest {
    private lateinit var autoDIViewModelFactory: AutoDIViewModelFactory
    private val cartRepository: CartRepository = FakeCartRepository()
    private val productRepository: ProductRepository = FakeProductRepository()

    @Before
    fun setUp() {
        val dependencies: Map<KClass<*>, Any> =
            mapOf(
                CartRepository::class to cartRepository,
                ProductRepository::class to productRepository,
            )
        autoDIViewModelFactory = AutoDIViewModelFactory(dependencies)
    }

    @Test
    fun `MainViewModel 생성 테스트`() {
        // given:
        // when:
        val viewModel =
            autoDIViewModelFactory.create(MainViewModel::class.java)

        // then:
        assertNotNull(viewModel)
    }

    @Test
    fun `CartViewModel 생성 테스트`() {
        // given:
        // when:
        val viewModel =
            autoDIViewModelFactory.create(CartViewModel::class.java)

        // then:
        assertNotNull(viewModel)
    }
}
