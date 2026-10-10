package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
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

    class ViewModelScopeTestRepository

    class ScopeTestViewModel : ViewModel() {
        @field:DependencyContainer.Inject
        lateinit var repository: ViewModelScopeTestRepository

        @field:DependencyContainer.Inject
        @field:RoomCart
        lateinit var cartRepository: CartRepository
    }

    @Test
    fun `ViewModel 의존성을 지정한 스코프에 생성하고 부모 앱 의존성을 공유한다`() {
        val firstScope = DependencyContainer.createScope()
        val secondScope = DependencyContainer.createScope()

        val firstViewModel =
            DependencyContainer.create(
                ScopeTestViewModel::class.java,
                firstScope,
            )

        val anotherViewModelInFirstScope =
            DependencyContainer.create(
                ScopeTestViewModel::class.java,
                firstScope,
            )

        val secondViewModel =
            DependencyContainer.create(
                ScopeTestViewModel::class.java,
                secondScope,
            )

        assertThat(firstViewModel.repository)
            .isSameInstanceAs(anotherViewModelInFirstScope.repository)
        assertThat(firstViewModel.repository)
            .isNotSameInstanceAs(secondViewModel.repository)
        assertThat(firstViewModel.cartRepository)
            .isSameInstanceAs(secondViewModel.cartRepository)
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

    @Test
    fun `ViewModelStore가 유지되는 동안 스코프를 재사용하고 비우면 새 스코프를 만든다`() {
        val firstStore = ViewModelStore()
        val firstProvider = ViewModelProvider(firstStore, DependencyContainer)
        val firstViewModel = firstProvider.get(ScopeTestViewModel::class.java)
        val reusedViewModel = firstProvider.get(ScopeTestViewModel::class.java)

        assertThat(reusedViewModel).isSameInstanceAs(firstViewModel)

        firstStore.clear()

        val secondStore = ViewModelStore()
        try {
            val secondViewModel =
                ViewModelProvider(secondStore, DependencyContainer)
                    .get(ScopeTestViewModel::class.java)

            assertThat(secondViewModel.repository)
                .isNotSameInstanceAs(firstViewModel.repository)
            assertThat(secondViewModel.cartRepository)
                .isSameInstanceAs(firstViewModel.cartRepository)
        } finally {
            secondStore.clear()
        }
    }

    @Test
    fun `Cart 화면에서 DateFormatter를 재사용하고 재진입하면 새로 생성한다`() {
        val firstStore = ViewModelStore()
        val firstViewModel =
            ViewModelProvider(firstStore, DependencyContainer)
                .get(CartViewModel::class.java)

        try {
            val retainedViewModel =
                ViewModelProvider(firstStore, DependencyContainer)
                    .get(CartViewModel::class.java)

            assertThat(retainedViewModel.dateFormatter)
                .isSameInstanceAs(firstViewModel.dateFormatter)
        } finally {
            firstStore.clear()
        }

        val secondStore = ViewModelStore()
        try {
            val reenteredViewModel =
                ViewModelProvider(secondStore, DependencyContainer)
                    .get(CartViewModel::class.java)

            assertThat(reenteredViewModel.dateFormatter)
                .isNotSameInstanceAs(firstViewModel.dateFormatter)
            assertThat(reenteredViewModel.cartRepository)
                .isSameInstanceAs(firstViewModel.cartRepository)
        } finally {
            secondStore.clear()
        }
    }

    @Test
    fun `Products 화면에서 ViewModel이 유지되면 Repository를 재사용하고 재진입하면 새로 생성한다`() {
        val firstStore = ViewModelStore()
        val firstViewModel =
            ViewModelProvider(firstStore, DependencyContainer)
                .get(ProductsViewModel::class.java)
        val firstRepository = firstViewModel.productRepository

        try {
            val retainedViewModel =
                ViewModelProvider(firstStore, DependencyContainer)
                    .get(ProductsViewModel::class.java)

            assertThat(retainedViewModel.productRepository)
                .isSameInstanceAs(firstRepository)
        } finally {
            firstStore.clear()
        }

        val secondStore = ViewModelStore()
        try {
            val reenteredViewModel =
                ViewModelProvider(secondStore, DependencyContainer)
                    .get(ProductsViewModel::class.java)

            assertThat(reenteredViewModel.productRepository)
                .isNotSameInstanceAs(firstRepository)
        } finally {
            secondStore.clear()
        }
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
