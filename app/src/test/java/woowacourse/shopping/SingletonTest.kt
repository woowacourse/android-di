package woowacourse.shopping

import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import shopping.di.DIContainer
import woowacourse.shopping.data.ProductRepository

class SingletonTest {

    @Before
    fun setUp() {
        DIContainer.register(ProductRepository::class.java, ProductRepository())
    }

    @Test
    fun `ProductRepository는 싱글톤으로 관리되어야 한다`() {
        // given
        val firstInstance = DIContainer.resolve(ProductRepository::class.java)
        val secondInstance = DIContainer.resolve(ProductRepository::class.java)

        // then
        assertSame(firstInstance, secondInstance)
    }
}
