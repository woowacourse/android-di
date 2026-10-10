package woowacourse.shopping

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.v2.createEmptyComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.ui.ShoppingNavHost
import woowacourse.shopping.ui.ShoppingRoute
import woowacourse.shopping.ui.cart.DateFormatter
import woowacourse.shopping.ui.cart.ScreenScopeViewModel
import woowacourse.shopping.util.annotations.RoomRepo

@RunWith(RobolectricTestRunner::class)
class ScreenScopeConfigurationTest {
    @get:Rule
    val composeRule = createEmptyComposeRule()

    @Test
    fun `화면 회전으로 Activity를 재생성해도 포맷터와 앱 저장소를 유지한다`() {
        val controller = Robolectric.buildActivity(ScopeTestActivity::class.java).setup()
        try {
            val activity = controller.get()
            val container = (RuntimeEnvironment.getApplication() as MyApplication).appContainer
            val repository = container.di.resolve(CartRepository::class, RoomRepo())
            composeRule.waitForIdle()
            composeRule.runOnIdle { activity.navController.navigate(ShoppingRoute.CART) }
            composeRule.waitForIdle()
            lateinit var first: ScreenScopeViewModel
            lateinit var formatter: DateFormatter
            lateinit var entryId: String
            composeRule.runOnIdle {
                val entry = activity.navController.currentBackStackEntry!!
                entryId = entry.id
                first = ViewModelProvider(entry)[ScreenScopeViewModel::class.java]
                formatter = container.di.resolve(DateFormatter::class, scope = first.scope) as DateFormatter
            }

            val configuration = Configuration(activity.resources.configuration)
            configuration.orientation =
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Configuration.ORIENTATION_PORTRAIT
                } else {
                    Configuration.ORIENTATION_LANDSCAPE
                }
            controller.configurationChange(configuration).visible()
            composeRule.waitForIdle()

            composeRule.runOnIdle {
                val recreated = controller.get()
                assertThat(recreated).isNotSameInstanceAs(activity)
                val entry = recreated.navController.currentBackStackEntry!!
                assertThat(entry.destination.route).isEqualTo(ShoppingRoute.CART)
                assertThat(entry.id).isEqualTo(entryId)
                val restored = ViewModelProvider(entry)[ScreenScopeViewModel::class.java]
                assertThat(restored).isSameInstanceAs(first)
                assertThat(first.scope.isClosed).isFalse()
                assertThat(container.di.resolve(DateFormatter::class, scope = restored.scope)).isSameInstanceAs(formatter)
                assertThat(container.di.resolve(CartRepository::class, RoomRepo())).isSameInstanceAs(repository)
                recreated.navController.popBackStack()
            }
            composeRule.waitForIdle()
            composeRule.runOnIdle {
                assertThat(first.scope.isClosed).isTrue()
                assertThat(first.scope.instanceCount).isEqualTo(0)
            }
        } finally {
            controller.pause().stop().destroy()
        }
    }
}

/** 재생성될 때도 실제 앱처럼 onCreate에서 내비게이션 UI를 다시 구성한다. */
class ScopeTestActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            ShoppingNavHost(navController)
        }
    }
}
