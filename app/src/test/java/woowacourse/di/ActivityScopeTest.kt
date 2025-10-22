package woowacourse.di

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.android.di.AndroidContainer
import woowacourse.shopping.android.di.Scope
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ActivityScopeTest {
    @Test
    fun `DateFormatter는 Activity LifeCycle 동안 유지된다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()

        val beforeRecreate = controller.get().dateFormatter

        controller.recreate()

        val afterRecreate = controller.get().dateFormatter

        // then
        assertThat(beforeRecreate).isNotSameInstanceAs(afterRecreate)
    }

    private class TestActivity : ComponentActivity() {
        lateinit var dateFormatter: DateFormatter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            AndroidContainer.register(DateFormatter::class, Scope.ActivityScope(this)) {
                DateFormatter(this)
            }

            dateFormatter =
                AndroidContainer.instance(
                    DateFormatter::class,
                    Scope.ActivityScope(this),
                )
        }
    }
}
