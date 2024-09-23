package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.example.alsonglibrary2.di.AutoDIManager
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: MainActivity
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        viewModel = ViewModelProvider(activity)[MainViewModel::class.java]
    }

    @Test
    fun `Activity 실행 테스트`() {
        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `MainActivity가 create 되면 ProductRepository의 인스턴스가 등록된다`() {
        // given
        activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // then
        assertThat(AutoDIManager.dependencies[ProductRepository::class]).isNotNull()
    }

    @Test
    fun `MainActivity가 destroy 되면 ProductRepository의 인스턴스가 사라진다`() {
        // given
        activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .destroy()
                .get()

        // then
        val actual = AutoDIManager.dependencies[ProductRepository::class]
        assertThat(actual).isNull()
    }
}
