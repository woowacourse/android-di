package olive.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import olive.di.fixture.ActivityScopeTestActivity1
import olive.di.fixture.ActivityScopeTestActivity2
import olive.di.fixture.Bar
import olive.di.fixture.TestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ActivityScopeTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `액티비티가 생성되면 인스턴스가 생성된다`() {
        // when
        val activity =
            Robolectric
                .buildActivity(ActivityScopeTestActivity1::class.java)
                .create()
                .get()

        // then
        val actual = activity.bar
        assertThat(instances[Bar::class]).isEqualTo(actual)
        assertThat(actual).isInstanceOf(Bar::class.java)
    }

    @Test
    fun `액티비티가 소멸되면 인스턴스가 소멸된다`() {
        // given
        val activityController =
            Robolectric
                .buildActivity(ActivityScopeTestActivity1::class.java)
                .create()

        // when
        activityController.destroy()

        // then
        assertThat(instances[Bar::class]).isNull()
    }

    @Test
    fun `두 액티비티가 서로 다른 인스턴스를 가진다`() {
        // when
        val activity1 =
            Robolectric
                .buildActivity(ActivityScopeTestActivity1::class.java)
                .create()
                .get()
        val activity2 =
            Robolectric
                .buildActivity(ActivityScopeTestActivity2::class.java)
                .create()
                .get()

        // then
        val actual1 = activity1.bar
        val actual2 = activity2.bar
        assertThat(actual1).isNotSameInstanceAs(actual2)
    }
}
