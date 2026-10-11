@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping.ui.cart

import android.app.Application
import android.content.Context
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.di.DependencyContainer
import woowacourse.shopping.ShoppingApplication

@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class CartScreenScopeTest {
    @get:Rule
    val composeRule = createComposeRule()

    private var isCartScreenVisible by mutableStateOf(true)
    private var currentScreenScopeId by mutableStateOf<Long?>(null)
    private var hasComposeContent = false

    @Before
    fun setUp() {
        DependencyContainer.openScope(ShoppingApplication.APP_SCOPE_ID)
        DependencyContainer.register(
            ShoppingApplication.APP_SCOPE_ID,
            Context::class,
            ApplicationProvider.getApplicationContext<Context>(),
        )
    }

    @After
    fun tearDown() {
        if (hasComposeContent) {
            composeRule.runOnIdle {
                isCartScreenVisible = false
            }
            composeRule.waitForIdle()
        }
        DependencyContainer.closeScope(ShoppingApplication.APP_SCOPE_ID)
    }

    @Test
    fun `화면 안에서는 DateFormatter를 재사용하고 나갔다 다시 들어오면 새로 생성한다`() {
        composeRule.setContent {
            if (isCartScreenVisible) {
                val screenScopeId = rememberScreenScopeId(ShoppingApplication.APP_SCOPE_ID)
                SideEffect {
                    currentScreenScopeId = screenScopeId
                }
            }
        }
        hasComposeContent = true
        composeRule.waitForIdle()

        var firstScreenScopeId = 0L
        composeRule.runOnIdle {
            firstScreenScopeId = checkNotNull(currentScreenScopeId)
        }
        val firstDateFormatter = getDateFormatter(firstScreenScopeId)
        val reusedDateFormatter = getDateFormatter(firstScreenScopeId)

        assertThat(reusedDateFormatter).isSameInstanceAs(firstDateFormatter)

        composeRule.runOnIdle {
            isCartScreenVisible = false
        }
        composeRule.waitForIdle()

        val closedScopeError =
            runCatching {
                getDateFormatter(firstScreenScopeId)
            }.exceptionOrNull()
        assertThat(closedScopeError).isInstanceOf(NoSuchElementException::class.java)

        composeRule.runOnIdle {
            isCartScreenVisible = true
        }
        composeRule.waitForIdle()

        var nextScreenScopeId = 0L
        composeRule.runOnIdle {
            nextScreenScopeId = checkNotNull(currentScreenScopeId)
        }
        val nextDateFormatter = getDateFormatter(nextScreenScopeId)

        assertThat(nextScreenScopeId).isNotEqualTo(firstScreenScopeId)
        assertThat(nextDateFormatter).isNotSameInstanceAs(firstDateFormatter)
    }

    private fun getDateFormatter(scopeId: Long): DateFormatter =
        DependencyContainer.getInstance(
            type = DateFormatter::class,
            scopeId = scopeId,
        ) as DateFormatter
}
