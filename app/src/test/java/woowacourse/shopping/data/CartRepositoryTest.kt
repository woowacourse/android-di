package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.ProductFixture

class CartRepositoryTest {
    private lateinit var repo: CartRepository

    @Before
    fun setUp() {
        repo = InMemoryCartRepository()
    }

    @Test
    fun add_product_in_cart() =
        runTest {
            // when
            repo.addCartProduct(ProductFixture(1))

            // then
            assertThat(repo.allCartProducts()).contains(ProductFixture(1))
        }

    @Test
    fun delete_product_in_cart() =
        runTest {
            // given
            repo =
                FakeCartRepository(
                    ProductFixture(1),
                    ProductFixture(2),
                )

            // when
            repo.deleteCartProduct(1)

            // then
            assertThat(repo.allCartProducts()).isEqualTo(
                listOf(
                    ProductFixture(2),
                ),
            )
        }
}
