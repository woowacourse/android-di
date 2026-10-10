package woowacourse.shopping

import com.example.di.SamDi
import com.example.di.annotations.InjectField
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.repository_impl.DefaultCartRepository
import woowacourse.shopping.data.repository_impl.FakeCartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.products.ProductsViewModel
import woowacourse.shopping.util.annotations.InMemoryRepo
import woowacourse.shopping.util.annotations.RoomRepo
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class SamDiTest {
    @Test
    fun `요청 타입의 provider가 있어도 Qualifier에 지정된 구현체를 선택한다`() {
        val application = RuntimeEnvironment.getApplication() as MyApplication
        val fallbackRepository = FakeCartRepository()
        var fallbackProviderCalls = 0
        val di =
            SamDi(
                providers =
                    mapOf(
                        CartRepository::class to {
                            fallbackProviderCalls++
                            fallbackRepository
                        },
                        CartProductDao::class to { application.appContainer.cartProductDao },
                    ),
                bindings = application.appContainer.bindings,
            )

        val roomConsumer = di.resolve(RoomCartRepositoryConsumer::class) as RoomCartRepositoryConsumer
        val inMemoryConsumer = di.resolve(InMemoryCartRepositoryConsumer::class) as InMemoryCartRepositoryConsumer

        assertThat(roomConsumer.repository).isInstanceOf(DefaultCartRepository::class.java)
        assertThat(inMemoryConsumer.repository).isInstanceOf(FakeCartRepository::class.java)
        assertThat(fallbackProviderCalls).isEqualTo(0)
    }

    @Test
    fun `Qualifier로 선택한 구현체에 provider가 있으면 그 provider의 객체를 주입한다`() {
        val application = RuntimeEnvironment.getApplication() as MyApplication
        val roomRepository = DefaultCartRepository(application.appContainer.cartProductDao)
        val inMemoryRepository = FakeCartRepository()
        val di =
            SamDi(
                providers =
                    mapOf(
                        DefaultCartRepository::class to { roomRepository },
                        FakeCartRepository::class to { inMemoryRepository },
                        CartProductDao::class to { application.appContainer.cartProductDao },
                    ),
                bindings = application.appContainer.bindings,
            )

        val roomConsumer = di.resolve(RoomCartRepositoryConsumer::class) as RoomCartRepositoryConsumer
        val inMemoryConsumer = di.resolve(InMemoryCartRepositoryConsumer::class) as InMemoryCartRepositoryConsumer

        assertThat(roomConsumer.repository).isSameInstanceAs(roomRepository)
        assertThat(inMemoryConsumer.repository).isSameInstanceAs(inMemoryRepository)
    }

    @Test
    fun `애노테이션이 붙은 ViewModel 필드만 주입한다`() {
        val application = RuntimeEnvironment.getApplication() as MyApplication
        val factory = ViewModelFactory.viewModelFactory(application)

        val productsViewModel = factory.create(ProductsViewModel::class.java)
        val fieldViewModel = factory.create(FieldInjectionViewModel::class.java)

        assertThat(productsViewModel.productRepository)
            .isNotSameInstanceAs(fieldViewModel.injected)
        assertThat(fieldViewModel.injected).isNotNull()
        assertThat(fieldViewModel.unannotated)
            .isSameInstanceAs(FieldInjectionViewModel.originalRepository)
    }

    @Test
    fun `CartRepository의 DAO 의존성을 재귀적으로 해결한다`() =
        runTest {
            val application = RuntimeEnvironment.getApplication() as MyApplication
            val consumer =
                application.appContainer.di.resolve(RoomCartRepositoryConsumer::class)
                    as RoomCartRepositoryConsumer
            val repository = consumer.repository
            val product = Product(name = "DI-${UUID.randomUUID()}", price = 1, imageUrl = "")

            assertThat(repository).isInstanceOf(DefaultCartRepository::class.java)

            repository.addCartProduct(product)
            val inserted = repository.getAllCartProducts().single { it.product.name == product.name }
            assertThat(inserted.product.price).isEqualTo(product.price)

            repository.deleteCartProduct(inserted.id)
            assertThat(repository.getAllCartProducts().none { it.id == inserted.id }).isTrue()
        }

    @Test
    fun `Room Qualifier는 Room 구현체를 선택한다`() {
        val application = RuntimeEnvironment.getApplication() as MyApplication

        val consumer =
            application.appContainer.di.resolve(RoomCartRepositoryConsumer::class)
                as RoomCartRepositoryConsumer

        assertThat(consumer.repository).isInstanceOf(DefaultCartRepository::class.java)
    }

    @Test
    fun `InMemory Qualifier는 InMemory 구현체를 선택한다`() {
        val application = RuntimeEnvironment.getApplication() as MyApplication

        val consumer =
            application.appContainer.di.resolve(InMemoryCartRepositoryConsumer::class)
                as InMemoryCartRepositoryConsumer

        assertThat(consumer.repository).isInstanceOf(FakeCartRepository::class.java)
    }

    @Test
    fun `구현체가 여러 개인 타입을 Qualifier 없이 요청하면 예외가 발생한다`() {
        val application = RuntimeEnvironment.getApplication() as MyApplication

        val exception =
            assertThrows(IllegalArgumentException::class.java) {
                application.appContainer.di.resolve(CartRepository::class)
            }

        assertThat(exception)
            .hasMessageThat()
            .contains("CartRepository")
        assertThat(exception)
            .hasMessageThat()
            .contains("qualifier")
    }
}

class RoomCartRepositoryConsumer(
    @RoomRepo val repository: CartRepository,
)

class InMemoryCartRepositoryConsumer(
    @InMemoryRepo val repository: CartRepository,
)

class FieldInjectionViewModel : ScopedViewModel() {
    companion object {
        val originalRepository = ProductRepository()
    }

    @InjectField
    lateinit var injected: ProductRepository

    var unannotated: ProductRepository = originalRepository
}
