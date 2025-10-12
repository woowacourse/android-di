package woowacourse.shopping.di

import io.mockk.mockk
import org.assertj.core.api.SoftAssertions
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel

class DependencyInjectorTest {
    private lateinit var cartRepository: CartRepository
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() {
        cartRepository = mockk<CartRepository>()
        productRepository = mockk<ProductRepository>()
    }

    @Test
    fun `인스턴스 생성 시 필드가 주입된다`() {
        // given
        DependencyInjector.setInstance(CartRepository::class, cartRepository, DatabaseLogger::class)

        // when
        val viewModel = DependencyInjector.getInstance(CartViewModel::class)
        DependencyInjector.injectAnnotatedProperties(CartViewModel::class, viewModel)

        // then
        val field = viewModel::class.java.getDeclaredField("cartRepository")
        field.isAccessible = true
        val actual = field.get(viewModel)
        Assert.assertEquals(cartRepository, actual)
    }

    @Test
    fun `어노테이션에 따라 올바른 구현체를 주입 받는다`() {
        // given
        val softly = SoftAssertions()

        DependencyInjector.setInstance(CartRepository::class, cartRepository, DatabaseLogger::class)
        DependencyInjector.setInstance(
            ProductRepository::class,
            productRepository,
            InMemoryLogger::class,
        )

        // when
        val databaseLogger =
            DependencyInjector.getInstance(CartRepository::class, qualifier = DatabaseLogger::class)
        val inMemoryLogger =
            DependencyInjector.getInstance(
                ProductRepository::class,
                qualifier = InMemoryLogger::class,
            )

        // then
        softly.assertThat(databaseLogger).isSameAs(cartRepository)
        softly.assertThat(inMemoryLogger).isSameAs(productRepository)
        softly.assertAll()
    }
}
