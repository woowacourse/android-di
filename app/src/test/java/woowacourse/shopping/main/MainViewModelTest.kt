package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.CoroutinesTestExtension
import woowacourse.extensions.getOrAwaitValue
import woowacourse.fake.FakeCartProductDao
import woowacourse.fake.FakeDatabaseModule
import woowacourse.fake.FakeRepositoryModule
import woowacourse.fixture.PRODUCTS_FIXTURE
import woowacourse.peto.di.DependencyContainer
import woowacourse.peto.di.ViewModelFactoryInjector
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.ui.main.vm.MainViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var dependencyContainer: DependencyContainer
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        dependencyContainer =
            DependencyContainer(
                listOf(
                    FakeDatabaseModule(),
                    FakeRepositoryModule(FakeCartProductDao()),
                ),
            )
        viewModel =
            ViewModelFactoryInjector(dependencyContainer)
                .create(MainViewModel::class.java)
    }

    @Test
    fun `전체 상품 목록을 가져온다`() {
        // given
        val products = PRODUCTS_FIXTURE

        // when
        viewModel.getAllProducts()

        // then
        assertEquals(products, viewModel.products.getOrAwaitValue())
    }

    @Test
    fun `장바구니에 상품을 추가한다`() =
        runTest {
            // given
            val newProduct =
                Product(
                    name = "감자",
                    price = 3000,
                    imageUrl = "",
                )

            // when
            viewModel.addCartProduct(newProduct)

            // then
            assertTrue(viewModel.onProductAdded.getOrAwaitValue())
        }
}
