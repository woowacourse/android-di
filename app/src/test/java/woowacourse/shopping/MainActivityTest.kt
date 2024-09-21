package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import com.woowa.di.test.DIActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val diRule = DIActivityTestRule(MainActivity::class.java)

    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        activity = diRule.getActivity()
    }

    @Test
    fun `Activity 실행 테스트`() {
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // ViewModelProvider를 통해 ViewModel 주입 여부 확인
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]
        assertThat(viewModel).isNotNull()
    }
}
