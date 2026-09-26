package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartProductEntity
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.InMemoryCart
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RoomCart
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class DependencyContainerTest {
    @Before
    fun setUp() {
        val cartProductDao =
            object : CartProductDao {
                override suspend fun getAll(): List<CartProductEntity> = emptyList()

                override suspend fun insert(cartProduct: CartProductEntity) = Unit

                override suspend fun delete(id: Long) = Unit
            }

        DependencyContainer.register(
            CartProductDao::class,
            cartProductDao,
        )
        DependencyContainer.register(
            CartRepository::class,
            RoomCart::class,
            RoomCartRepository(cartProductDao),
        )
        DependencyContainer.register(
            CartRepository::class,
            InMemoryCart::class,
            InMemoryCartRepository(),
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
        assertThat(cartViewModel.cartRepository).isInstanceOf(RoomCartRepository::class.java)
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

    @Test
    fun `Qualifier 없이 요청하면 사용 가능한 Qualifier를 안내한다`() {
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

        val exception =
            runCatching {
                DependencyContainer.create(UnqualifiedQualifierTestViewModel::class.java)
            }.exceptionOrNull()

        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
        assertThat(exception?.message).contains("TestRepository")
        assertThat(exception?.message).contains("Local")
        assertThat(exception?.message).contains("Remote")
    }

    @Test
    fun `Qualifier 메타 애노테이션이 없는 애노테이션은 의존성 식별자로 사용하지 않는다`() {
        val repository = LocalUnqualifiedRepository()
        DependencyContainer.register(UnqualifiedRepository::class, repository)

        val viewModel = DependencyContainer.create(NonQualifierTestViewModel::class.java)

        assertThat(viewModel.repository).isSameInstanceAs(repository)
    }

    @Test
    fun `InMemory Qualifier로 실제 InMemory 구현체를 선택한다`() {
        val viewModel = DependencyContainer.create(InMemoryCartTestViewModel::class.java)

        assertThat(viewModel.cartRepository).isInstanceOf(InMemoryCartRepository::class.java)
    }

    interface TestRepository

    private class LocalRepository : TestRepository

    private class RemoteRepository : TestRepository

    @DependencyContainer.Qualifier
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Local

    @DependencyContainer.Qualifier
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class Remote

    class QualifierTestViewModel : ViewModel() {
        @field:DependencyContainer.Inject
        @field:Local
        lateinit var localRepository: TestRepository

        @field:DependencyContainer.Inject
        @field:Remote
        lateinit var remoteRepository: TestRepository
    }

    class UnqualifiedQualifierTestViewModel : ViewModel() {
        @field:DependencyContainer.Inject
        lateinit var repository: TestRepository
    }

    interface UnqualifiedRepository

    private class LocalUnqualifiedRepository : UnqualifiedRepository

    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    private annotation class DisplayOnly

    class NonQualifierTestViewModel : ViewModel() {
        @field:DependencyContainer.Inject
        @field:DisplayOnly
        lateinit var repository: UnqualifiedRepository
    }

    class InMemoryCartTestViewModel : ViewModel() {
        @field:DependencyContainer.Inject
        @field:InMemoryCart
        lateinit var cartRepository: CartRepository
    }
}
