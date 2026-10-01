package woowacourse.shopping.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.di.Inject
import woowacourse.di.get
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class ShoppingInjectionTest {
    @Test
    fun `Factory가 애플리케이션 컨테이너를 통해 ViewModel에서 Room까지 연결한다`() =
        runTest {
            val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
            val extras = MutableCreationExtras().apply { set(APPLICATION_KEY, application) }
            val products = ViewModelFactory.create(ProductsViewModel::class.java, extras)
            val cart = ViewModelFactory.create(CartViewModel::class.java, extras)
            val database = application.injector.create(ShoppingDatabase::class)
            try {
                assertThat(products.cartRepository).isSameAs(cart.cartRepository)
                assertThat(cart.cartRepository).isInstanceOf(DefaultCartRepository::class.java)
                assertThat(application.injector.create(CartProductDao::class))
                    .isSameAs(database.cartProductDao())

                products.cartRepository.addCartProduct(Product("과자", 10_000, ""))
                val saved = cart.cartRepository.getAllCartProducts().single()
                assertThat(saved.name).isEqualTo("과자")
                assertThat(
                    database
                        .cartProductDao()
                        .getAll()
                        .single()
                        .id,
                ).isEqualTo(saved.id)
            } finally {
                database.close()
            }
        }

    @Test
    fun `애플리케이션 정보가 없으면 필요한 의존성을 오류로 알린다`() {
        assertThatThrownBy {
            ViewModelFactory.create(CartViewModel::class.java, CreationExtras.Empty)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("ShoppingApplication이 필요합니다")
    }

    @Test
    fun `앱의 두 저장소를 Qualifier로 주입하고 각각 독립적으로 사용한다`() =
        runTest {
            val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
            val injector = application.injector
            val consumer = injector.get<RepositoryConsumer>()
            val database = injector.get<ShoppingDatabase>()
            try {
                assertThat(consumer.room).isInstanceOf(DefaultCartRepository::class.java)
                assertThat(consumer.memory).isInstanceOf(InMemoryCartRepository::class.java)
                assertThat(consumer.room).isSameAs(injector.get<CartRepository>(RoomCart::class))
                assertThat(consumer.memory).isSameAs(injector.get<CartRepository>(InMemoryCart::class))

                consumer.room.addCartProduct(Product("Room 상품", 10_000, ""))
                assertThat(consumer.memory.getAllCartProducts()).isEmpty()
                consumer.memory.addCartProduct(Product("메모리 상품", 8_000, ""))

                assertThat(consumer.room.getAllCartProducts().map { it.name }).containsExactly("Room 상품")
                assertThat(consumer.memory.getAllCartProducts().map { it.name }).containsExactly("메모리 상품")
                consumer.memory.deleteCartProduct(
                    consumer.memory
                        .getAllCartProducts()
                        .single()
                        .id,
                )
                assertThat(consumer.memory.getAllCartProducts()).isEmpty()
                assertThat(consumer.room.getAllCartProducts()).hasSize(1)
            } finally {
                database.close()
            }
        }

    @Test
    fun `앱에서도 Qualifier 없는 장바구니 요청은 두 후보를 알리며 실패한다`() {
        val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()

        assertThatThrownBy { application.injector.get<CartRepository>() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Qualifier가 필요합니다: CartRepository")
            .hasMessageContaining("RoomCart")
            .hasMessageContaining("InMemoryCart")
    }

    class RepositoryConsumer {
        @Inject
        @RoomCart
        lateinit var room: CartRepository

        @Inject
        @InMemoryCart
        lateinit var memory: CartRepository
    }
}
