package woowacourse.shopping.di

import com.example.seogi.di.DiContainer
import com.example.seogi.di.annotation.InMemory
import com.example.seogi.di.annotation.OnDisk
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.fixture.FakeCartProductDao
import woowacourse.shopping.fixture.FakeCartRepositoryInMemory
import woowacourse.shopping.fixture.FakeCartRepositoryOnDisk
import woowacourse.shopping.fixture.FakeModule
import woowacourse.shopping.fixture.FakeProductRepository
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository

class DiContainerTest {
    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터X, 어노테이션X)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(ProductRepository::class, null)

        // then
        assertThat(actual).isEqualTo(FakeProductRepository)
    }

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터X, , 어노테이션O)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(CartRepository::class, InMemory())

        // then
        assertThat(actual).isEqualTo(FakeCartRepositoryInMemory.getInstance())
    }

    @Test
    fun `클래스 타입에 따른 인스턴스를 가져온다(생성자 파라미터O, , 어노테이션O우)`() {
        // given
        val diContainer = DiContainer(FakeModule)

        // when
        val actual = diContainer.getInstance(CartRepository::class, OnDisk())

        // then
        assertThat(actual).isEqualTo(FakeCartRepositoryOnDisk.getInstance(FakeCartProductDao))
    }
}
