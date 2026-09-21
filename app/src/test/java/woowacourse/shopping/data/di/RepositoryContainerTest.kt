package woowacourse.shopping.data.di

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository

class RepositoryContainerTest {
    @Test
    fun `어노테이션이 붙은 필드에만 의존성이 주입된다`() {
        val container = RepositoryContainer()
        val target = InjectionTarget()

        container.inject(target)

        assertThat(target.productRepository)
            .isSameInstanceAs(container.getInstance(ProductRepository::class))

        assertThat(target.cartRepository)
            .isSameInstanceAs(container.getInstance(CartRepository::class))


        assertThat(target.productNotInjected).isNull()
        assertThat(target.cartNotInjected).isNull()
    }

    private class InjectionTarget {
        @field:CustomFieldInjection
        lateinit var productRepository: ProductRepository

        @field:CustomFieldInjection
        lateinit var cartRepository: CartRepository

        var productNotInjected: ProductRepository? = null

        var cartNotInjected: CartRepository? = null
    }
}