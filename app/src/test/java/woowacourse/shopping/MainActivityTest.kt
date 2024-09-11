package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
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
    fun `MainViewModel이 상품들을 가져올 수 있어야 한다`() {
        // then
        assertThat(viewModel.products.getOrAwaitValue()[0].name).isEqualTo("우테코 과자")
        assertThat(viewModel.products.getOrAwaitValue()[1].name).isEqualTo("우테코 쥬스")
        assertThat(viewModel.products.getOrAwaitValue()[2].name).isEqualTo("우테코 아이스크림")
    }
}
