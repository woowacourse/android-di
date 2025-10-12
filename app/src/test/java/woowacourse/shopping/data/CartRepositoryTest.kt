package woowacourse.shopping.data

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import woowacourse.shopping.data.mapper.toDomain
import woowacourse.shopping.model.Product

class CartRepositoryTest {
    @Test
    fun `상품을_추가하면_장바구니에_저장된다`() =
        runTest {
            // given
            val dao = mockk<CartProductDao>(relaxed = true)
            val repository: CartRepository = CartRepositoryImpl(dao)
            val product = Product("A", 1000, "a")

            // when
            repository.addCartProduct(product)

            // then
            coVerify(exactly = 1) {
                dao.insert(
                    withArg { entity ->
                        assertThat(entity.name).isEqualTo("A")
                        assertThat(entity.price).isEqualTo(1000)
                        assertThat(entity.imageUrl).isEqualTo("a")
                    },
                )
            }
        }

    @Test
    fun `장바구니를_조회하면_추가한_상품들이_순서대로_나온다`() =
        runTest {
            // given
            val dao = mockk<CartProductDao>()
            val a = CartProductEntity("A", 1000, "a").apply { id = 1L }
            val b = CartProductEntity("B", 2000, "b").apply { id = 2L }
            coEvery { dao.getAll() } returns listOf(a, b)
            val repository: CartRepository = CartRepositoryImpl(dao)

            // when
            val result = repository.getAllCartProducts()

            // then
            assertThat(result.map { it }).containsExactly(a.toDomain(), b.toDomain()).inOrder()
            coVerify(exactly = 1) { dao.getAll() }
        }

    @Test
    fun `장바구니에서_ID에_따라_상품을_삭제하면_해당_상품이_제거된다`() =
        runTest {
            // given
            val dao = mockk<CartProductDao>(relaxed = true)
            val repository: CartRepository = CartRepositoryImpl(dao)

            // when
            repository.deleteCartProduct(1L)

            // then
            coVerify(exactly = 1) { dao.delete(1L) }
        }
}
