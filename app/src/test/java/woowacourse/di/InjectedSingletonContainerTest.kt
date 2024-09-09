package woowacourse.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.di.InjectedComponent
import woowacourse.shopping.di.InjectedSingletonContainer

class InjectedSingletonContainerTest {
    @Test
    fun `싱글톤 의존성 컨테이너에 기본 상품 저장소 구현체를 넣고, 해당 인터페이스 타입으로 확인한다`() {
        // when
        InjectedSingletonContainer.add(
            InjectedComponent.InjectedSingletonComponent(
                ProductRepository::class,
                DefaultProductRepository(),
            ),
        )

        // then
        val productRepository = InjectedSingletonContainer.find(ProductRepository::class)
        assertThat(productRepository).isInstanceOf(DefaultProductRepository::class.java)
    }

    @Test
    fun `싱글톤 의존성 컨테이너에 기본 장바구니 저장소 구현체를 넣고, 해당 인터페이스 타입으로 확인한다 `() {
        // when
        InjectedSingletonContainer.add(
            InjectedComponent.InjectedSingletonComponent(
                CartRepository::class,
                DefaultCartRepository(),
            ),
        )

        // then
        val cartRepository = InjectedSingletonContainer.find(CartRepository::class)
        assertThat(cartRepository).isInstanceOf(DefaultCartRepository::class.java)
    }
}
