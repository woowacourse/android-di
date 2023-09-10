package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import woowacourse.shopping.di.annotation.CustomInject
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.fake.testDatas
import woowacourse.shopping.model.Product
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.isAccessible

class MainViewModelTest {

    private lateinit var cartRepository: CartRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var viewModel: MainViewModel

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        cartRepository = FakeCartRepository()
        productRepository = FakeProductRepository()
        viewModel = MainViewModel()

        // viewModel 필드 주입
        viewModel::class.declaredMemberProperties.filter { it.hasAnnotation<CustomInject>() }
            .forEach {
                it.isAccessible = true
                if (it.returnType == CartRepository::class.starProjectedType) {
                    (it as KMutableProperty<*>).setter.call(viewModel, cartRepository)
                } else {
                    (it as KMutableProperty<*>).setter.call(viewModel, productRepository)
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품들을 불러온다`() {
        // given & when
        viewModel.getAllProducts()

        // then
        assertEquals(viewModel.products.value, testDatas)
    }

    @Test
    fun `상품을 카트에 추가할 수 있다`() {
        // given
        val product = Product(
            name = "우테코 과자",
            price = 10_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
        )
        assertEquals(viewModel.onProductAdded.value, false)

        // when
        viewModel.addCartProduct(product)

        // then
        assertEquals(viewModel.onProductAdded.value, true)
    }
}
