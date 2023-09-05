package woowacourse.shopping

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

class FakeCartViewModel(private val repository: FakeCartRepository) {
    fun getProducts(): List<String> = repository.getAllCartProducts().map { it.name }
}

class ReflectionTest {
    private val repositories = mapOf(
        FakeProductRepository::class.starProjectedType to FakeProductRepository(),
        FakeCartRepository::class.starProjectedType to FakeCartRepository(),
    )

    @Test
    fun `변경 불가능한 비공개 프로퍼티 값 설정하여 객체 생성`() {
        // given
        val modelClass = FakeCartViewModel::class.java
        val constructor = modelClass.kotlin.primaryConstructor

        // when
        val args = constructor?.parameters?.associateWith { repositories[it.type] } ?: emptyMap()
        val viewModel = constructor?.callBy(args)

        // then
        val actual = viewModel?.getProducts()
        val expected = listOf("milk")
        assertThat(actual).isEqualTo(expected)
    }
}
