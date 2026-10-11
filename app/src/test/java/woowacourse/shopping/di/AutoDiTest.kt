package woowacourse.shopping.di

import com.cksckckcks.di.AutoDi
import com.cksckckcks.di.InjectProperty
import com.cksckckcks.di.ScopeType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.ShoppingApplication
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
        val container = ShoppingContainer(RuntimeEnvironment.getApplication()).diContainer

        val target = AutoDi(container.applicationScope).createInstance(InjectionTarget::class)

        assertThat(target.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, LocalMemoryCart::class))
        assertThat(target.isUnannotatedInitialized()).isFalse()
    }

    @Test
    fun `CartRepository의 DAO 의존성을 재귀적으로 해결한다`() {
        runBlocking {
            val container = ShoppingContainer(RuntimeEnvironment.getApplication()).diContainer
            val target = AutoDi(container.applicationScope).createInstance(RecursiveTarget::class)
            val dao = container.getInstance(CartProductDao::class) as CartProductDao
            val countBefore = dao.getAll().size

            target.cartRepository.addCartProduct(Product("재귀 주입 테스트 상품", 1_000, ""))

            assertThat(target.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, LocalMemoryCart::class))
            assertThat(dao.getAll()).hasSize(countBefore + 1)
        }
    }

    @Test
    fun `Qualifier에 따라 서로 다른 CartRepository를 주입한다`() {
        val container = ShoppingContainer(RuntimeEnvironment.getApplication()).diContainer
        val autoDi = AutoDi(container.applicationScope)

        val localTarget = autoDi.createInstance(InjectionTarget::class)
        val memoryTarget = autoDi.createInstance(InMemoryTarget::class)
        val constructorTarget = autoDi.createInstance(ConstructorTarget::class)

        assertThat(localTarget.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, LocalMemoryCart::class))
        assertThat(memoryTarget.cartRepository).isInstanceOf(InMemoryCartRepository::class.java)
        assertThat(memoryTarget.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, InMemoryCart::class))
        assertThat(constructorTarget.cartRepository).isSameInstanceAs(memoryTarget.cartRepository)
    }

    @Test
    fun `서로 다른 스코프에도 같은 앱의 CartRepository를 주입한다`() {
        val application = RuntimeEnvironment.getApplication() as ShoppingApplication
        val container = application.container.diContainer
        val viewModelScope = container.openScope(ScopeType("viewModel"), "products")
        val screenScope = container.openScope(ScopeType("screen"), "cart")

        try {
            val products = AutoDi(viewModelScope).createInstance(InjectionTarget::class)
            val cart = AutoDi(screenScope).createInstance(InjectionTarget::class)
            val productsInMemory = AutoDi(viewModelScope).createInstance(InMemoryTarget::class)
            val cartInMemory = AutoDi(screenScope).createInstance(InMemoryTarget::class)

            assertThat(products.cartRepository).isSameInstanceAs(cart.cartRepository)
            assertThat(products.cartRepository).isSameInstanceAs(container.getInstance(CartRepository::class, LocalMemoryCart::class))
            assertThat(productsInMemory.cartRepository).isSameInstanceAs(cartInMemory.cartRepository)
            assertThat(productsInMemory.cartRepository).isNotSameInstanceAs(products.cartRepository)
        } finally {
            viewModelScope.close()
            screenScope.close()
        }
    }

    @Test
    fun `개별 스코프를 종료해도 앱 저장소와 장바구니 데이터는 유지된다`() {
        runBlocking {
            val application = RuntimeEnvironment.getApplication() as ShoppingApplication
            val container = application.container.diContainer
            val screen = ScopeType("screen")
            val firstScope = container.openScope(screen, "cart")
            val first = AutoDi(firstScope).createInstance(InMemoryTarget::class)
            val localRepository = firstScope.getInstance(CartRepository::class, LocalMemoryCart::class)
            val product = Product("앱 스코프 테스트 상품", 1_000, "")
            try {
                first.cartRepository.addCartProduct(product)
            } finally {
                firstScope.close()
            }

            val reopenedScope = container.openScope(screen, "cart")
            try {
                val reopened = AutoDi(reopenedScope).createInstance(InMemoryTarget::class)

                assertThat(reopened.cartRepository).isSameInstanceAs(first.cartRepository)
                assertThat(reopened.cartRepository.getAllCartProducts().map { it.name }).contains(product.name)
                assertThat(reopenedScope.getInstance(CartRepository::class, LocalMemoryCart::class)).isSameInstanceAs(localRepository)
            } finally {
                reopenedScope.close()
            }
        }
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
