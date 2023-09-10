package woowacourse.shopping.study

import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import woowacourse.shopping.fake.FakeCartRepository
import woowacourse.shopping.fake.FakeProductRepository
import woowacourse.shopping.getCartProducts
import woowacourse.shopping.getProducts
import woowacourse.shopping.model.CartProduct
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

class FakeCartViewModel(private val repository: FakeCartRepository) {
    suspend fun getProducts(): List<CartProduct> = repository.getAllCartProducts()
}

class ReflectionTest {
    private val repositories = mapOf(
        FakeProductRepository::class.starProjectedType to FakeProductRepository(),
        FakeCartRepository::class.starProjectedType to FakeCartRepository(getCartProducts().toMutableList()),
    )

    @Test
    fun `변경 불가능한 비공개 프로퍼티 값 설정하여 객체 생성`() = runTest {
        // given
        val modelClass = FakeCartViewModel::class.java
        val constructor = modelClass.kotlin.primaryConstructor

        // when
        val args = constructor?.parameters?.associateWith { repositories[it.type] } ?: emptyMap()
        val viewModel = constructor?.callBy(args)

        // then
        val actual = viewModel?.getProducts()
        val expected = getCartProducts()
        assertThat(actual).isEqualTo(expected)
    }
}
