package woowacourse.shopping.hilt.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import woowacourse.shopping.hilt.data.CartRepository
import woowacourse.shopping.hilt.ui.cart.CartActivity
import woowacourse.shopping.hilt.ui.cart.DateFormatter

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@Config(application = HiltTestApplication::class)
class CartActivityTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val scenarioRule = activityScenarioRule<CartActivity>()
    private val scenario get() = scenarioRule.scenario

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `Activity 실행 테스트`() {
        scenario.onActivity { activity ->
            activity.shouldNotBeNull()
        }
    }

    @Test
    fun `DateFormatter 주입 테스트`() {
        scenario.onActivity { activity ->
            activity.dateFormatter.shouldNotBeNull()
        }
    }

    @Test
    fun `DateFormatter 는 ConfigureChange 에도 동일한 인스턴스 주입받는다`() {
        // given
        var dateFormatter: DateFormatter? = null
        scenario.onActivity { activity ->
            dateFormatter = activity.dateFormatter
        }
        // when : 파괴 후 재생성
        scenario.recreate()
        // then
        scenario.onActivity { activity ->
            activity.dateFormatter shouldBe dateFormatter
        }
    }

    @Test
    fun `Activity 파괴 시 DateFormatter도 파괴된다`() {
        // given
        var dateFormatter: DateFormatter? = null
        scenario.onActivity { activity ->
            dateFormatter = activity.dateFormatter
        }
        // when : 파괴 후 재생성
        scenario.moveToState(Lifecycle.State.DESTROYED)
        scenario.close()
        val newScenario = launchActivity<CartActivity>()
        // then
        newScenario.onActivity { activity ->
            activity.dateFormatter shouldNotBe dateFormatter
        }
    }

    @Test
    fun `CartRepository 는 ConfigureChange 에도 동일한 인스턴스 주입받는다`() {
        var cartRepository: CartRepository? = null
        scenario.onActivity { activity ->
            cartRepository = activity.viewModel.cartRepository
        }
        // when : 파괴 후 재생성
        scenario.recreate()
        // then
        scenario.onActivity { activity ->
            activity.viewModel.cartRepository shouldBe cartRepository
        }
    }

    @Test
    fun `Activity 파괴 시 CartRepository 도 파괴된다`() {
        var cartRepository: CartRepository? = null
        scenario.onActivity { activity ->
            cartRepository = activity.viewModel.cartRepository
        }
        // when
        scenario.moveToState(Lifecycle.State.DESTROYED)
        scenario.close()
        val newScenario = launchActivity<CartActivity>()
        // then
        newScenario.onActivity { activity ->
            activity.viewModel.cartRepository shouldNotBe cartRepository
        }
    }
}