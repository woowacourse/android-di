package woowacourse.shopping.cart

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.SoftAssertions
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.mapper.toEntity
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.fixture.TEST_PRODUCT

class CartRepositoryImplTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var dao: CartProductDao
    private lateinit var cartRepository: CartRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        dao = mockk<CartProductDao>()
        cartRepository = CartRepositoryImpl(dao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `상품을 추가하면 장바구니에 해당 상품이 존재한다`() =
        runTest {
            // given
            val softly = SoftAssertions()
            coEvery { dao.insert(TEST_PRODUCT.toEntity()) } returns Unit
            coEvery { dao.getAll() } returns listOf(TEST_PRODUCT).map { it.toEntity() }
            cartRepository.addCartProduct(TEST_PRODUCT)

            // when
            val actual = cartRepository.getAllCartProducts().getOrNull()

            // then
            coVerify { dao.insert(TEST_PRODUCT.toEntity()) }
            coVerify { dao.getAll() }
            softly.assertThat(actual).isNotNull
            softly.assertThat(actual!!.first().id).isEqualTo(1L)
            softly.assertThat(actual.first().name).isEqualTo("우테코 과자")
            softly.assertThat(actual.first().price).isEqualTo(10_000)
            softly.assertAll()
        }

    @Test
    fun `상품을 제거하면 장바구니에 해당 상품이 존재하지 않는다`() =
        runTest {
            // given
            coEvery { dao.insert(TEST_PRODUCT.toEntity()) } returns Unit
            cartRepository.addCartProduct(TEST_PRODUCT)
            coEvery { dao.delete(1L) } returns Unit
            coEvery { dao.getAll() } returns emptyList()

            // when
            cartRepository.deleteCartProduct(1L)
            val actual = cartRepository.getAllCartProducts().getOrNull()

            // then
            coVerify { dao.insert(TEST_PRODUCT.toEntity()) }
            coVerify { dao.delete(1L) }
            coVerify { dao.getAll() }
            Assert.assertEquals(emptyList<Product>(), actual)
        }
}
