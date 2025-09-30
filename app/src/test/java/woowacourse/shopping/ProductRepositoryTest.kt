package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.containerProvider
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class ProductRepositoryTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val application =
        RuntimeEnvironment.getApplication()

    @Test
    fun `모든 상품을 불러올 수 있다`() {
        // given
        val productRepository by application.containerProvider<ProductRepository>()
        val expected =
            listOf(
                "우테코 과자",
                "우테코 쥬스",
                "우테코 아이스크림",
            )

        // when
        val actual =
            productRepository.getAllProducts().map {
                it.name
            }

        // then
        assertThat(actual).isEqualTo(expected)
    }
}
