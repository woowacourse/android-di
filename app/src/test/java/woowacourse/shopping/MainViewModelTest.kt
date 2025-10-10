package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainViewModel

class MainViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    lateinit var viewModel: MainViewModel
    lateinit var fakeCartRepository: FakeCardRepository
    lateinit var fakeProductRepository: FakeProductRepository

    private val fakeProduct =
        Product(
            name = "테스트 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        )

    @Before
    fun setup() {
        // given
        fakeCartRepository = FakeCardRepository(emptyList())
        fakeProductRepository = FakeProductRepository(fakeProducts)
        viewModel =
            MainViewModel(fakeProductRepository, fakeCartRepository)
    }

    @Test
    fun `상품을_카트에_담을_수_있다`() {
        // when
        viewModel.addCartProduct(fakeProduct)
        // then
        assertThat(fakeCartRepository.getAllCartProducts().contains(fakeProduct)).isTrue
    }

    @Test
    fun `상품을_가져올_수_있다`() {
        // when
        viewModel.getAllProducts()
        // then
        assertThat(viewModel.products.value).isEqualTo(fakeProducts)
    }
}
