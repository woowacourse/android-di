package woowacourse.shopping

import com.android.di.component.DiSingletonComponent
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.data.di.RepositoryModule
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SingletonTest {
    @Before
    fun setup() {
        RepositoryModule.install()
    }

    @Test
    fun `ProductRepository는 Singleton으로 생성되어야 한다`() {
        val firstInstance = DiSingletonComponent.match(ProductRepository::class)
        val secondInstance = DiSingletonComponent.match(ProductRepository::class)

        assertTrue(firstInstance === secondInstance)
    }

    @Test
    fun `CartRepository는 Singleton으로 생성되어야 한다`() {
        val firstInstance = DiSingletonComponent.match(CartRepository::class)
        val secondInstance = DiSingletonComponent.match(CartRepository::class)

        assertTrue(firstInstance === secondInstance)
    }
}
