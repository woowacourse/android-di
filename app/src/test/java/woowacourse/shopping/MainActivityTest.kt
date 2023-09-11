package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import com.example.di.application.DiApplication
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel
import woowacourse.util.FakeApplicationModule

class FakeApplication : DiApplication(FakeApplicationModule::class.java)

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `FakeApplicationModule 초기화 실행 테스트`() {
        // given
        val application =
            ApplicationProvider.getApplicationContext<DiApplication>()

        // then
        assertThat(application is DiApplication).isEqualTo(true)
    }

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric.buildActivity(MainActivity::class.java).create().get()

        // when
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }
}
