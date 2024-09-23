package olive.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import olive.di.fixture.Baz
import olive.di.fixture.TestApplication
import olive.di.fixture.ViewModelScopeTestActivity1
import olive.di.fixture.ViewModelScopeTestActivity2
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.IllegalArgumentException

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewModelScopeTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `뷰모델이 생성되면 인스턴스가 생성된다`() {
        // when
        val activity =
            Robolectric
                .buildActivity(ViewModelScopeTestActivity1::class.java)
                .create()
                .get()

        // then
        val actual = activity.viewModel.baz
        assertThat(instances[Baz::class]).isEqualTo(actual)
        assertThat(actual).isInstanceOf(Baz::class.java)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `뷰모델이 소멸되면 인스턴스가 소멸된다`() {
        // given
        val activityController =
            Robolectric
                .buildActivity(ViewModelScopeTestActivity1::class.java)
                .setup()

        // when
        activityController.destroy()

        // then
        assertThat(instances[Baz::class])
    }

    @Test
    fun `두 뷰모델이 서로 다른 인스턴스를 가진다`() {
        // when
        val activity1 =
            Robolectric
                .buildActivity(ViewModelScopeTestActivity1::class.java)
                .create()
                .get()
        val activity2 =
            Robolectric
                .buildActivity(ViewModelScopeTestActivity2::class.java)
                .create()
                .get()

        // then
        val actual1 = activity1.viewModel.baz
        val actual2 = activity2.viewModel.baz
        assertThat(actual1).isNotSameInstanceAs(actual2)
    }
}
