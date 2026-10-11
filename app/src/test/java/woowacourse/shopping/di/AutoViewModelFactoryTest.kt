@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import woowacourse.di.DependencyContainer
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartProductDao
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.qualifier.Room
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class AutoViewModelFactoryTest {
    private lateinit var viewModelStore: ViewModelStore
    private val additionalViewModelStores = mutableListOf<ViewModelStore>()

    class ScopedRepositoryViewModel(
        val firstProductRepository: ProductRepository,
        val secondProductRepository: ProductRepository,
        @param:Room val cartRepository: CartRepository,
    ) : ScopedViewModel()

    class FakeRepository : CartRepository {
        override suspend fun addCartProduct(product: Product) {}

        override suspend fun getAllCartProducts(): List<CartProduct> = emptyList()

        override suspend fun deleteCartProduct(id: Long) {}
    }

    @Before
    fun setUp() {
        DependencyContainer.openScope(ShoppingApplication.APP_SCOPE_ID)
        viewModelStore = ViewModelStore()

        DependencyContainer.register(
            ShoppingApplication.APP_SCOPE_ID,
            CartProductDao::class,
            FakeCartProductDao(),
        )
        DependencyContainer.register(
            ShoppingApplication.APP_SCOPE_ID,
            CartRepository::class,
            Room::class,
            FakeRepository(),
        )
    }

    @After
    fun tearDown() {
        viewModelStore.clear()
        additionalViewModelStores.forEach(ViewModelStore::clear)
        DependencyContainer.closeScope(ShoppingApplication.APP_SCOPE_ID)
    }

    @Test
    fun `CartViewModel을 요청하면 CartViewModel이 생성된다`() {
        val viewModel =
            ViewModelProvider(viewModelStore, AutoViewModelFactory())
                .get(CartViewModel::class.java)

        assertEquals(viewModel::class.java, CartViewModel::class.java)
    }

    @Test
    fun `ProductsViewModel을 요청하면 ProductsViewModel이 생성된다`() {
        val viewModel =
            ViewModelProvider(viewModelStore, AutoViewModelFactory())
                .get(ProductsViewModel::class.java)

        assertEquals(viewModel::class.java, ProductsViewModel::class.java)
    }

    @Test
    fun `앱 스코프 CartRepository는 공유하고 ViewModel 스코프 ProductRepository는 ViewModel과 함께 정리된다`() {
        val anotherViewModelStore = ViewModelStore().also(additionalViewModelStores::add)
        val firstViewModel =
            ViewModelProvider(viewModelStore, AutoViewModelFactory())
                .get(ScopedRepositoryViewModel::class.java)
        val secondViewModel =
            ViewModelProvider(anotherViewModelStore, AutoViewModelFactory())
                .get(ScopedRepositoryViewModel::class.java)

        assertSame(firstViewModel.firstProductRepository, firstViewModel.secondProductRepository)
        assertNotSame(firstViewModel.firstProductRepository, secondViewModel.firstProductRepository)
        assertSame(firstViewModel.cartRepository, secondViewModel.cartRepository)

        viewModelStore.clear()

        val afterClearStore = ViewModelStore().also(additionalViewModelStores::add)
        val afterClearViewModel =
            ViewModelProvider(afterClearStore, AutoViewModelFactory())
                .get(ScopedRepositoryViewModel::class.java)

        assertNotSame(firstViewModel.firstProductRepository, afterClearViewModel.firstProductRepository)
        assertSame(firstViewModel.cartRepository, afterClearViewModel.cartRepository)
    }
}
