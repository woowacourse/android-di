package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.di.containerProvider
import woowacourse.shopping.domain.model.Product

@RunWith(RobolectricTestRunner::class)
class CartRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val application =
        RuntimeEnvironment.getApplication()

    @Test
    fun `상품을 삭제할 수 있다`() {
        // given
        val cartRepository by application.containerProvider<DefaultCartRepository>()
        cartRepository.addCartProduct(Product("상품명", 1000, "이미지URL"))
        val target = cartRepository.getAllCartProducts().first()
        val id = 0

        // when
        cartRepository.deleteCartProduct(id)

        // then
        val actual = cartRepository.getAllCartProducts()
        assertThat(actual).doesNotContain(target)
    }

    @Test
    fun `상품을 추가할 수 있다`() {
        // given
        val cartRepository by application.containerProvider<DefaultCartRepository>()
        val product = Product("상품명", 1000, "이미지URL")

        // when
        cartRepository.addCartProduct(product)

        // then
        val actual = cartRepository.getAllCartProducts()
        assertThat(actual).contains(product)
    }

    @Test
    fun `상품을 조회할 수 있다`() {
        // given
        val cartRepository by application.containerProvider<DefaultCartRepository>()
        val product = Product("상품명", 1000, "이미지URL")
        cartRepository.addCartProduct(product)
        val expected = listOf("상품명")

        // when
        cartRepository.getAllCartProducts()

        // then
        val actual = cartRepository.getAllCartProducts().map { it.name }
        assertThat(actual).isEqualTo(expected)
    }
}
