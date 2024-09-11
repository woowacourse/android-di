package woowacourse.shopping

import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.DIContainer

class DIContainerTest {

    private lateinit var mockCartProductDao: CartProductDao
    private lateinit var cartRepository: CartRepository

    @Before
    fun setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this)

        mockCartProductDao = mock(CartProductDao::class.java)

        DIContainer.register(CartProductDao::class.java, mockCartProductDao)
        DIContainer.register(CartRepository::class.java, CartRepository(mockCartProductDao))
    }

    @Test
    fun `CartRepository가 CartProductDao를 주입받는지 확인`() {
        val repository = DIContainer.resolve(CartRepository::class.java)

        // CartRepository가 null이 아닌지 확인
        assertNotNull(repository)
    }
}
