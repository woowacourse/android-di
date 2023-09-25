package woowacourse.shopping.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `acitivity의 container가 recreate되면 새로운 컨테이너가 생성된다`() {
        // given
        val activityController = buildActivity(MainActivity::class.java)
        val activity = activityController.create().get()

        // when
        val origin = activity.container
        activityController.recreate()
        val new = activity.container

        // then
        assertNotSame(origin, new)
    }

    @Test
    fun `acitivity의 container가 Destroy된 후 새로운 activity가 생성되면 새 컨테이너가 생성된다`() {
        // given
        val activityController = buildActivity(MainActivity::class.java)
        val activity = activityController.create().get()

        // when
        val origin = activity.container
        activityController.pause().stop().destroy()
        val new = activity.container

        // then
        assertNotSame(origin, new)
    }

    @Test
    fun `acitivity의 container가 구성 변경 되어도 새로운 컨테이너가 생성되지 않는다`() {
        // given
        val activityController = buildActivity(MainActivity::class.java)
        val activity = activityController.create().get()

        // when
        val origin = activity.container
        activityController.configurationChange()
        val new = activity.container

        // then
        assertSame(origin, new)
    }
}
