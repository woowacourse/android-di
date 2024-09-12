package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import com.woowa.di.injection.ModuleRegistry
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.dao.DaoDI
import woowacourse.shopping.fixture.TestApplication
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.di.dao.DaoModule
import woowacourse.shopping.di.repository.RepositoryDI
import woowacourse.shopping.di.repository.RepositoryModule

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class CartActivityTest {

    private lateinit var activity: CartActivity

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val controller = Robolectric.buildActivity(CartActivity::class.java)
        ModuleRegistry.registerModule(RepositoryDI::class, RepositoryModule::class)
        ModuleRegistry.registerModule(
            DaoDI::class, DaoModule::class
        )
        if (DaoModule.getInstanceOrNull() == null) {
            DaoModule.initLifeCycle(controller.get())
            DaoModule.getInstance().onCreate(controller.get() as LifecycleOwner)
        }
        if (RepositoryModule.getInstanceOrNull() == null) {
            RepositoryModule.initLifeCycle(controller.get())
            RepositoryModule.getInstance().onCreate(controller.get() as LifecycleOwner)
        }
        activity = controller.create().get()
    }

    @Test
    fun `Activity 실행 테스트`() {
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val viewModel = ViewModelProvider(activity)[CartViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }
}