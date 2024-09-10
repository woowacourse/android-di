package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowa.shopping.di.libs.container.Containers
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        Containers.clearContainersForTest()
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
}
