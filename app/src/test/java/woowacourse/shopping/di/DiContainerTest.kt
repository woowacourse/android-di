package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartProductDao
import woowacourse.shopping.fixture.FakeCartRepository
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class DiContainerTest {
    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터가 없는 경우)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(ProductRepository::class)

        // then
        assertThat(actual).isEqualTo(FakeProductRepository)
    }

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터가 있는 경우)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(CartRepository::class)

        // then
        assertThat(actual).isEqualTo(FakeCartRepository.getInstance(FakeCartProductDao))
    }
}
