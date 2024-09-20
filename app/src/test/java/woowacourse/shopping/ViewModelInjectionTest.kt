package woowacourse.shopping

import android.content.Context
import com.android.di.component.DiSingletonComponent
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.createRoomDatabase
import woowacourse.shopping.data.di.annotation.RoomDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.ui.MainViewModel
import woowacourse.shopping.ui.cart.CartViewModel

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class ViewModelInjectionTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = RuntimeEnvironment.getApplication()

        DiSingletonComponent.bind(CartRepository::class, CartRepositoryImpl::class)
        DiSingletonComponent.bind(ProductRepository::class, ProductRepositoryImpl::class)
        val database = createRoomDatabase(context)
        DiSingletonComponent.provide(
            RoomDatabase::class,
            database.cartProductDao(),
        )
    }

    @Test
    fun `CartViewModel의 의존성 주입이 정상적으로 이루어졌는지 확인한다`() {
        val factory = inject()
        val viewModel = factory.create(CartViewModel::class.java)

        assertNotNull(viewModel)
        try {
            viewModel.getAllCartProducts()
        } catch (e: UninitializedPropertyAccessException) {
            throw AssertionError(e.message)
        }
    }

    @Test
    fun `MainViewModel의 의존성 주입이 정상적으로 이루어졌는지 확인한다`() {
        val factory = inject()
        val viewModel = factory.create(MainViewModel::class.java)

        assertNotNull(viewModel)
        try {
            viewModel.getAllProducts()
        } catch (e: UninitializedPropertyAccessException) {
            throw AssertionError(e.message)
        }
    }
}
