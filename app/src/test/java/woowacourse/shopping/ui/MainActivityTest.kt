package woowacourse.shopping.ui

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.common.truth.Truth.assertThat
import com.now.androdi.activity.ActivityInjectable
import com.now.annotation.Inject
import com.now.di.DependencyType
import com.now.di.Module
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowActivity
import woowacourse.shopping.R
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.ViewModelFactory
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.main.MainActivity
import woowacourse.shopping.ui.main.MainViewModel

interface FakeRepository

class DefaultFakeRepository : FakeRepository

class FakeModule : Module {
    fun provideFakeRepository(): FakeRepository {
        return DefaultFakeRepository()
    }
}

class FakeViewModel(
    @Inject val fakeRepository: FakeRepository,
) : ViewModel()

class FakeActivityWithDependency : ActivityInjectable() {
    val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(this))[FakeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.addModule(FakeModule())
    }
}

class FakeActivityWithoutDependency : ActivityInjectable() {
    val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(this))[FakeViewModel::class.java]
    }
}

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
    }

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `장바구니 버튼을 클릭하면 장바구니 화면으로 이동한다`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        val menu = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).menu
        activity.onCreateOptionsMenu(menu)

        val cartItem = menu.findItem(R.id.cart).actionView!!

        // when
        cartItem.performClick()

        val shadowActivity: ShadowActivity = Shadows.shadowOf(activity)
        val startedIntent = shadowActivity.nextStartedActivity

        // then
        assertEquals(startedIntent.component?.className, CartActivity::class.java.name)
    }

    @Test
    fun `리사이클러뷰 아이템 개수는 DefaultProductRepository의 아이템 수와 같다`() {
        // given
        val activityController = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .start()

        val activity = activityController.get()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_products)

        // when
        val productRepository = DefaultProductRepository()
        val actual = productRepository.getAllProducts().size

        // then
        assertEquals(recyclerView.adapter?.itemCount, actual)
    }

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivityWithDependency::class.java)
            .create()
            .get()

        val viewModel = activity.viewModel

        val container = activity.injector.getCurrentContainer()
        val dependencyType = DependencyType(FakeRepository::class, null)
        val fakeRepository = container.getInstance(dependencyType)

        assertNotNull(viewModel)
        assertEquals(viewModel.fakeRepository, fakeRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `적절한 객체 인스턴가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivityWithoutDependency::class.java)
            .create()
            .get()

        activity.viewModel
    }
}
