package woowacourse.shopping.hasydi

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertNotNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.hasydi.annotation.Inject

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class LifecycleTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `FakeActivity를 생성하면 dateformatter가 주입된다`() {
        // when
        val activity = Robolectric
            .buildActivity(FakeActivityWithDateFormatter::class.java)
            .create()
            .get()

        // then
        assertNotNull(activity.dateFormatter)
    }

    @Test
    fun `구성 변경 시 dateformatter가 유지된다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivityWithDateFormatter::class.java)
            .create()
        val before = activity.get().dateFormatter

        // when
        activity.configurationChange()

        // then
        val after = activity.get().dateFormatter
        assertThat(after).isSameAs(before)
    }
}

class FakeApplication : DiApplication(
    activityRetainedModule = listOf(FakeActivityWithDateFormatter::class to FakeLifecycleModule()),
)

class FakeLifecycleModule : Module {
    override var context: Context? = null

    fun provideDateFormatter() = FakeDateFormatter()
}

class FakeActivityWithDateFormatter : DiActivity() {
    @Inject
    lateinit var dateFormatter: FakeDateFormatter
}

class FakeDateFormatter
