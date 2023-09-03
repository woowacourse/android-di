package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.AutoInjector
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.module.Module
import woowacourse.shopping.global.ShoppingApplication
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeInjector: Injector

    @Before
    fun setup() {
        val cartRepository = DefaultCartRepository()
        fakeInjector = AutoInjector(
            listOf(
                object : Module {
                    private val cartRepository = DefaultCartRepository()
                    fun getCartRepository(): CartRepository = cartRepository
                },
                object : Module {
                    fun getProductRepository(): ProductRepository {
                        return object : ProductRepository {
                            override fun getAllProducts(): List<Product> {
                                return listOf(
                                    Product(
                                        name = "사과",
                                        price = 10_000,
                                        imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/711c39ee-ff8e-4983-aa01-f669e82bddae.jpg?h=700&w=700",
                                    ),
                                    Product(
                                        name = "포도",
                                        price = 8_000,
                                        imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/d4218dc8-7f21-4211-aabd-0f72a2a54b92.jpg?h=700&w=700",
                                    ),
                                )
                            }
                        }
                    }
                },
            ),
        )
        val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
        val injector = ShoppingApplication::class.java.getDeclaredField("injector")
        injector.apply {
            isAccessible = true
            set(application, fakeInjector)
        }
    }

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `MainViewModel 상품 목록 조회 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        viewModel.getAllProducts()

        // then
        assertThat(viewModel.products.getOrAwaitValue()).isEqualTo(
            listOf(
                Product(
                    name = "사과",
                    price = 10_000,
                    imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/711c39ee-ff8e-4983-aa01-f669e82bddae.jpg?h=700&w=700",
                ),
                Product(
                    name = "포도",
                    price = 8_000,
                    imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/d4218dc8-7f21-4211-aabd-0f72a2a54b92.jpg?h=700&w=700",
                ),
            ),
        )
    }
}
