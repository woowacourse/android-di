package woowacourse.shopping.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lope.di.annotation.CustomInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.ui.cart.CartViewModel
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.isAccessible

class CartViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: CartViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = FakeCartRepository()
        viewModel = CartViewModel()
        // viewModel 필드 주입
        viewModel::class.declaredMemberProperties.first { it.hasAnnotation<CustomInject>() }
            .let {
                it.isAccessible = true
                (it as KMutableProperty<*>).setter.call(viewModel, cartRepository)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `장바구니 상품들을 불러온다`() {
        // given & when
        viewModel.getAllCartProducts()

        // then
        assert(viewModel.cartProducts.value?.size == 3)
    }

    @Test
    fun `장바구니 상품들을 삭제할 수 있다`() {
        // given
        val id = 0L
        assert(viewModel.onCartProductDeleted.value == false)

        // when
        viewModel.deleteCartProduct(id)

        // then
        assert(viewModel.onCartProductDeleted.value == true)
    }
}
