package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.peto.di.ViewModelFactoryInjector
import woowacourse.shopping.ui.main.MainActivity
import woowacourse.shopping.ui.main.vm.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `MainViewModel 주입 테스트`() {
        // given
        val activity =
            Robolectric.buildActivity(MainActivity::class.java)
                .create()
                .get()

        val appContainer = (activity.application as App).container
        val factory = ViewModelFactoryInjector(appContainer.dependencyContainer)

        // when
        val viewModel = ViewModelProvider(activity, factory)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }
}
