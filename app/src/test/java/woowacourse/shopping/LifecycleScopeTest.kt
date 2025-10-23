package woowacourse.shopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.app.ShoppingApplication
import woowacourse.shopping.app.ui.MainViewModel
import woowacourse.shopping.app.ui.cart.DateFormatter
import woowacourse.shopping.di.DIViewModelFactory
import woowacourse.shopping.di.resolve
import kotlin.getValue

class ExampleActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels {
        val container = ShoppingApplication.getContainer(this)
        DIViewModelFactory(container)
    }
    lateinit var dateFormatter: DateFormatter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = ShoppingApplication.getContainer(applicationContext)
        dateFormatter = container.resolve<DateFormatter>() as DateFormatter
    }
}

@RunWith(RobolectricTestRunner::class)
class LifecycleScopeTest {
    @Test
    fun `구성 변경시 ViewModel과 ProductRepository는 유지, 완전 종료 후엔 ProductRepository 새로 생성`() {
        val initialController = Robolectric.buildActivity(ExampleActivity::class.java).setup()
        val initialViewModel = initialController.get().viewModel

        val initialCartRepository = initialViewModel.cartRepository
        val initialProductRepository = initialViewModel.productRepository

        // 구성 변경
        val recreateController = initialController.recreate()
        val recreateViewModel = recreateController.get().viewModel

        val recreateCartRepository = recreateViewModel.cartRepository
        val recreateProductRepository = recreateViewModel.productRepository

        // ViewModel과 ProductRepository는 유지되어야 한다.
        assertThat(recreateViewModel).isSameInstanceAs(initialViewModel)
        assertThat(recreateProductRepository).isSameInstanceAs(initialProductRepository)
        // CartRepository(@Singleton)은 동일해야 한다.
        assertThat(recreateCartRepository).isSameInstanceAs(initialCartRepository)

        // 완전 종료 후 새 Activity 생성
        recreateController.pause().stop().destroy()
        val newActivityController = Robolectric.buildActivity(ExampleActivity::class.java).setup()
        val newViewModel = newActivityController.get().viewModel

        val newActivityCartRepository = newViewModel.cartRepository
        val newActivityProductRepository = newViewModel.productRepository

        // CartRepository(@Singleton)은 앱 전역에서 동일해야 한다.
        assertThat(newActivityCartRepository).isSameInstanceAs(initialCartRepository)
        // ProductRepository는 새 ViewModel이므로 새 인스턴스여야 한다.
        assertThat(newActivityProductRepository).isNotSameInstanceAs(initialProductRepository)
    }

    @Test
    fun `구성 변경시 DateFormatter는 유지되며, 완전 종료 후엔 새 인스턴스가 생성된다`() {
        val initialController = Robolectric.buildActivity(ExampleActivity::class.java).setup()
        val initialDateFormatter = initialController.get().dateFormatter

        // 구성 변경
        val recreateController = initialController.recreate()
        val recreateDateFormatter = recreateController.get().dateFormatter

        // 구성 변경 시 액티비티 생명주기이기 때문에 유지되면 안된다
        assertThat(recreateDateFormatter).isNotSameInstanceAs(initialDateFormatter)

        // 완전 종료 후 새 Activity 생성
        recreateController.pause().stop().destroy()
        val newActivityController = Robolectric.buildActivity(ExampleActivity::class.java).setup()
        val newActivityDateFormatter = newActivityController.get().dateFormatter

        // 완전 종료 후에도 새 인스턴스여야 한다.
        assertThat(newActivityDateFormatter).isNotSameInstanceAs(initialDateFormatter)
    }
}
