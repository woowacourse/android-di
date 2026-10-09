package woowacourse.shopping

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.cksckckcks.di.ScopedContainer
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.di.ShoppingScopes
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.cart.DateFormatter

@RunWith(RobolectricTestRunner::class)
class ScreenScopeConfigurationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `실제 장바구니 화면은 구성 변경에 포맷터를 유지하고 종료할 때 참조를 제거한다`() {
        composeRule.onNodeWithContentDescription("장바구니").performClick()
        composeRule.onNodeWithText("장바구니").assertExists()
        val container = (RuntimeEnvironment.getApplication() as ShoppingApplication).container.diContainer
        val scopes = container.readPrivateField("scopes") as Map<*, *>
        val scope = scopes.values.filterIsInstance<ScopedContainer>().single { it.type == ShoppingScopes.screen }
        val formatter = scope.getInstance(DateFormatter::class)
        val instances = scope.readPrivateField("instances") as Map<*, *>
        val activity = composeRule.activity

        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()

        composeRule.onNodeWithText("장바구니").assertExists()
        assertThat(composeRule.activity).isNotSameInstanceAs(activity)
        val retained = scopes.values.filterIsInstance<ScopedContainer>().single { it.type == ShoppingScopes.screen }
        assertThat(retained).isSameInstanceAs(scope)
        assertThat(retained.getInstance(DateFormatter::class)).isSameInstanceAs(formatter)

        composeRule.activityRule.scenario.close()

        assertThat(scopes).isEmpty()
        assertThat(instances).isEmpty()
        assertThat(scope.readPrivateField("container")).isNull()
    }

    private fun Any.readPrivateField(name: String): Any? = javaClass.getDeclaredField(name).apply { isAccessible = true }.get(this)
}
