package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.InjectingViewModelFactory
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.fake.FakeViewModel
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel
import kotlin.reflect.KClass
import kotlin.reflect.KType

class InjectingViewModelFactoryTest {
    private lateinit var factory: InjectingViewModelFactory
    private val productRepository: ProductRepository = FakeProductRepository()
    private val cartRepository: CartRepository = FakeCartRepository()

    @Before
    fun setup() {
        val dependencies: Map<KClass<*>, Any> =
            mapOf(
                CartRepository::class to cartRepository,
                ProductRepository::class to productRepository,
            )
        val container = MapBackedContainer(dependencies)
        factory = InjectingViewModelFactory(container)
    }

    @Test
    fun `MainViewModel 생성 테스트`() {
        // when
        val viewModel = factory.create(MainViewModel::class.java)

        // then
        assertNotNull(viewModel)
        assertThat(viewModel).isInstanceOf(MainViewModel::class.java)
    }

    @Test
    fun `CartViewModel 생성 테스트`() {
        // when
        val viewModel = factory.create(CartViewModel::class.java)

        // then
        assertNotNull(viewModel)
        assertThat(viewModel).isInstanceOf(CartViewModel::class.java)
    }

    @Test(expected = IllegalStateException::class)
    fun `바인딩 누락 시 예외를 던진다`() {
        // given
        val dependencies: Map<KClass<*>, Any> =
            mapOf(
                CartRepository::class to cartRepository,
            )
        val container = MapBackedContainer(dependencies)
        val factory = InjectingViewModelFactory(container)

        // when
        factory.create(MainViewModel::class.java)
    }

    @Test
    fun `@Inject가 붙은 필드만 주입된다`() {
        // when
        val viewModel = factory.create(FakeViewModel::class.java)

        // then
        assertThat(viewModel.productRepository).isSameAs(productRepository)
        assertThat(viewModel.notInjected).isNull()
    }

    private class MapBackedContainer(
        private val map: Map<KClass<*>, Any>,
    ) : AppContainer {
        override fun resolve(type: KType): Any {
            val kClass =
                type.classifier as? KClass<*>
                    ?: error("Unsupported type: $type")
            return map[kClass] ?: error("No binding for $kClass")
        }
    }
}
