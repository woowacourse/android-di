package woowacourse.shopping.di

import com.cksckckcks.di.AutoDi
import com.cksckckcks.di.InjectProperty
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.LocalMemoryCart
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class AutoDiTest {
    @Test
    fun `애노테이션이 붙은 필드만 주입한다`() {
        val container = ShoppingContainer(RuntimeEnvironment.getApplication())

        val target = AutoDi(container).createInstance(InjectionTarget::class)

        assertThat(target.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, LocalMemoryCart::class))
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

            assertThat(target.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, LocalMemoryCart::class))
            assertThat(dao.getAll()).hasSize(countBefore + 1)
        }
    }

    @Test
    fun `Qualifier에 따라 서로 다른 CartRepository를 주입한다`() {
        val container = ShoppingContainer(RuntimeEnvironment.getApplication())
        val autoDi = AutoDi(container)

        val localTarget = autoDi.createInstance(InjectionTarget::class)
        val memoryTarget = autoDi.createInstance(InMemoryTarget::class)
        val constructorTarget = autoDi.createInstance(ConstructorTarget::class)

        assertThat(localTarget.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, LocalMemoryCart::class))
        assertThat(memoryTarget.cartRepository).isInstanceOf(InMemoryCartRepository::class.java)
        assertThat(memoryTarget.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, InMemoryCart::class))
        assertThat(constructorTarget.cartRepository).isSameInstanceAs(memoryTarget.cartRepository)
    }

    class InjectionTarget {
        @InjectProperty
        @LocalMemoryCart
        lateinit var cartRepository: CartRepository

        lateinit var unannotated: CartRepository

        fun isUnannotatedInitialized(): Boolean = this::unannotated.isInitialized
    }

    class RecursiveTarget(
        @LocalMemoryCart val cartRepository: CartRepository,
    )

    class InMemoryTarget {
        @InjectProperty
        @InMemoryCart
        lateinit var cartRepository: CartRepository
    }

    class ConstructorTarget(
        @InMemoryCart val cartRepository: CartRepository,
    )
}
