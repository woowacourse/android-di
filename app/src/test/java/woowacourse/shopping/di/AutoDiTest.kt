package woowacourse.shopping.di

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class AutoDiTest {
    @Test
    fun `애노테이션이 붙은 필드만 주입한다`() {
        val container = ShoppingContainer(RuntimeEnvironment.getApplication())

        val target = AutoDi(container).createInstance(InjectionTarget::class)

        assertThat(target.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class))
        assertThat(target.isUnannotatedInitialized()).isFalse()
    }

    @Test
    fun `CartRepository의 DAO 의존성을 재귀적으로 해결한다`() {
        runBlocking {
            val container = ShoppingContainer(RuntimeEnvironment.getApplication())
            val target = AutoDi(container).createInstance(RecursiveTarget::class)
            val dao = container.getInstance(CartProductDao::class) as CartProductDao
            val countBefore = dao.getAll().size

            target.cartRepository.addCartProduct(Product("재귀 주입 테스트 상품", 1_000, ""))

            assertThat(target.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class))
            assertThat(dao.getAll()).hasSize(countBefore + 1)
        }
    }

    class InjectionTarget {
        @InjectProperty
        lateinit var cartRepository: CartRepository

        lateinit var unannotated: CartRepository

        fun isUnannotatedInitialized(): Boolean = this::unannotated.isInitialized
    }

    class RecursiveTarget(
        val cartRepository: CartRepository,
    )
}
