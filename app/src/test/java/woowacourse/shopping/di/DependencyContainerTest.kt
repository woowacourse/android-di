package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DependencyContainerTest {

    @Before
    fun setUp() {
        DependencyContainer.registerCartProductDao(
            object : CartProductDao {
                override suspend fun getAll(): List<CartProductEntity> = emptyList()

                override suspend fun insert(cartProduct: CartProductEntity) = Unit

                override suspend fun delete(id: Long) = Unit
            },
        )
    }

    class FieldInjectionTestViewModel : ViewModel() {
        @field:DependencyContainer.Inject
        lateinit var injectedRepository: ProductRepository

        var notInjectedRepository: ProductRepository? = null
    }

    @Test
    fun `Inject가 붙은 필드에만 의존성을 주입한다`() {
        val viewModel =
            DependencyContainer.create(FieldInjectionTestViewModel::class.java)

        assertThat(viewModel.injectedRepository).isNotNull()
        assertThat(viewModel.notInjectedRepository).isNull()
    }

    @Test
    fun `ViewModel을 자동 생성한다`() {
        val productsViewModel = DependencyContainer.create(ProductsViewModel::class.java)
        val cartViewModel = DependencyContainer.create(CartViewModel::class.java)

        assertThat(productsViewModel).isNotNull()
        assertThat(cartViewModel).isNotNull()
    }

    @Test
    fun `두 ViewModel이 같은 Repository를 공유한다`() {
        val productsViewModel = DependencyContainer.create(ProductsViewModel::class.java)
        val cartViewModel = DependencyContainer.create(CartViewModel::class.java)

        assertThat(productsViewModel.cartRepository).isSameInstanceAs(cartViewModel.cartRepository)
    }

    @Test
    fun `필드 의존성의 의존성까지 재귀적으로 주입한다`() {
        val viewModel =
            DependencyContainer.create(CartViewModel::class.java)

        assertThat(viewModel.cartRepository).isNotNull()
    }

    @Test
    fun `Qualifier를 통해 구현체를 주입한다`() {
        DependencyContainer.register(
            TestRepository::class,
            Local::class,
            LocalRepository(),
        )
        DependencyContainer.register(
            TestRepository::class,
            Remote::class,
            RemoteRepository(),
        )

        val viewModel =
            DependencyContainer.create(QualifierTestViewModel::class.java)

        assertThat(viewModel.localRepository)
            .isInstanceOf(LocalRepository::class.java)

        assertThat(viewModel.remoteRepository)
            .isInstanceOf(RemoteRepository::class.java)

    }
    interface TestRepository
    private class LocalRepository : TestRepository
    private class RemoteRepository : TestRepository

    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Local

    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Remote

    class QualifierTestViewModel : ViewModel(){
        @field:DependencyContainer.Inject
        @field:Local
        lateinit var localRepository: TestRepository

        @field:DependencyContainer.Inject
        @field:Remote
        lateinit var remoteRepository: TestRepository
    }
}