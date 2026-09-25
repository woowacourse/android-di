package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertThrows
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class DIContainerTest {
    private val cartProductDao = FakeCartProductDao()
    private val container =
        DIContainer().apply {
            register(CartProductDao::class, cartProductDao)
            bind(CartRepository::class, DefaultCartRepository::class, RoomCart::class)
        }

    @Test
    fun `타입으로 인스턴스를 생성한다`() {
        val productRepository = container.get(ProductRepository::class)

        assertThat(productRepository).isInstanceOf(ProductRepository::class.java)
    }

    @Test
    fun `생성자에 필요한 의존성을 자동으로 주입한다`() {
        val viewModel = container.get(ProductsViewModel::class)

        assertThat(viewModel).isInstanceOf(ProductsViewModel::class.java)
    }

    @Test
    fun `애노테이션이 붙은 ViewModel 필드에만 의존성을 주입한다`() {
        val viewModel = container.get(FieldInjectionViewModel::class)

        assertThat(viewModel.injectedRepository).isSameInstanceAs(container.get(ProductRepository::class))
        assertThrows(UninitializedPropertyAccessException::class.java) { viewModel.notInjectedRepository }
    }

    @Test
    fun `같은 타입은 최초 생성한 인스턴스를 재사용한다`() {
        val first = container.get(CartRepository::class)

        val second = container.get(CartRepository::class)

        assertThat(second).isSameInstanceAs(first)
    }

    @Test
    fun `인터페이스 구현체의 생성자 의존성을 재귀적으로 주입한다`() {
        val repository = container.get(CartRepository::class)

        assertThat(repository).isInstanceOf(DefaultCartRepository::class.java)
    }

    @Test
    fun `Qualifier에 해당하는 구현체를 주입한다`() {
        val qualifiedContainer = createQualifiedContainer()

        val roomRepository = qualifiedContainer.get(CartRepository::class, RoomCart::class)
        val inMemoryRepository = qualifiedContainer.get(CartRepository::class, InMemoryCart::class)

        assertThat(roomRepository).isInstanceOf(DefaultCartRepository::class.java)
        assertThat(inMemoryRepository).isInstanceOf(InMemoryCartRepository::class.java)
    }

    @Test
    fun `주입 지점의 Qualifier에 해당하는 구현체를 필드에 주입한다`() {
        val qualifiedContainer = createQualifiedContainer()

        val viewModel = qualifiedContainer.get(QualifiedFieldInjectionViewModel::class)

        assertThat(viewModel.cartRepository).isInstanceOf(DefaultCartRepository::class.java)
    }

    @Test
    fun `같은 타입의 구현체가 여러 개이고 Qualifier가 없으면 예외가 발생한다`() {
        val qualifiedContainer = createQualifiedContainer()

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                qualifiedContainer.get(CartRepository::class)
            }

        assertThat(exception).hasMessageThat().contains("CartRepository 의존성이 모호합니다")
        assertThat(exception).hasMessageThat().contains("RoomCart")
        assertThat(exception).hasMessageThat().contains("InMemoryCart")
    }

    @Test
    fun `ViewModel은 다시 요청하면 새로 생성한다`() {
        val first = container.get(CartViewModel::class)

        val second = container.get(CartViewModel::class)

        assertThat(second).isNotSameInstanceAs(first)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `상품 화면에서 담은 상품을 장바구니 화면에서 조회한다`() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        try {
            val productsViewModel = container.get(ProductsViewModel::class)
            val cartViewModel = container.get(CartViewModel::class)
            val product = container.get(ProductRepository::class).getAllProducts().first()

            productsViewModel.addCartProduct(product)
            cartViewModel.getAllCartProducts()

            val cartProducts = cartViewModel.uiState.value.cartProducts
            val cartProduct = cartProducts.single()
            assertThat(cartProduct.name).isEqualTo(product.name)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun `순환 의존성의 경로를 오류로 알려준다`() {
        val exception = assertThrows(IllegalArgumentException::class.java) { container.get(FirstDependency::class) }

        assertThat(exception).hasMessageThat().contains("FirstDependency → SecondDependency → FirstDependency")
    }

    class FirstDependency(
        val second: SecondDependency,
    )

    class SecondDependency(
        val first: FirstDependency,
    )

    class FieldInjectionViewModel : androidx.lifecycle.ViewModel() {
        @Inject
        lateinit var injectedRepository: ProductRepository

        lateinit var notInjectedRepository: ProductRepository
    }

    class QualifiedFieldInjectionViewModel : androidx.lifecycle.ViewModel() {
        @Inject
        @RoomCart
        lateinit var cartRepository: CartRepository
    }

    private class FakeCartProductDao : CartProductDao {
        private val entities = mutableListOf<CartProductEntity>()
        private var nextId = 1L

        override suspend fun getAll(): List<CartProductEntity> = entities.toList()

        override suspend fun insert(cartProduct: CartProductEntity) {
            cartProduct.id = nextId++
            entities.add(cartProduct)
        }

        override suspend fun delete(id: Long) {
            entities.removeAll { entity -> entity.id == id }
        }
    }

    private fun createQualifiedContainer(): DIContainer =
        DIContainer().apply {
            register(CartProductDao::class, FakeCartProductDao())
            bind(CartRepository::class, DefaultCartRepository::class, RoomCart::class)
            bind(CartRepository::class, InMemoryCartRepository::class, InMemoryCart::class)
        }
}
