package woowacourse.shopping.hilt

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.kotest.matchers.nulls.shouldNotBeNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import woowacourse.shopping.hilt.ui.MainActivity

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class MainActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val scenarioRule = activityScenarioRule<MainActivity>()
    private val scenario get() = scenarioRule.scenario

    @Test
    fun `Activity 실행 테스트`() {
        scenario.onActivity { activity ->
            activity.shouldNotBeNull()
        }
    }
}
