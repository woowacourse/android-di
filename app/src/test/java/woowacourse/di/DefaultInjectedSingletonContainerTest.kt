package woowacourse.di

import com.example.sh1mj1.DefaultInjectedSingletonContainer
import com.example.sh1mj1.InjectedComponent
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.FakeCartRepository
import woowacourse.shopping.data.InMemoryProductRepository
import woowacourse.shopping.data.ProductRepository

class DefaultInjectedSingletonContainerTest {
    @Test
    fun `싱글톤 의존성 컨테이너에 기본 상품 저장소 구현체를 넣고, 해당 인터페이스 타입으로 확인한다`() {
        // when
        DefaultInjectedSingletonContainer.add(
            InjectedComponent.InjectedSingletonComponent(
                ProductRepository::class,
                InMemoryProductRepository(),
            ),
        )

        // then
        val productRepository = DefaultInjectedSingletonContainer.find(ProductRepository::class)
        assertThat(productRepository).isInstanceOf(InMemoryProductRepository::class.java)
    }

    @Test
    fun `싱글톤 의존성 컨테이너에 기본 장바구니 저장소 구현체를 넣고, 해당 인터페이스 타입으로 확인한다 `() {
        // when
        DefaultInjectedSingletonContainer.add(
            InjectedComponent.InjectedSingletonComponent(
                CartRepository::class,
                FakeCartRepository(),
            ),
        )

        // then
        val cartRepository = DefaultInjectedSingletonContainer.find(CartRepository::class)
        assertThat(cartRepository).isInstanceOf(FakeCartRepository::class.java)
    }
}
