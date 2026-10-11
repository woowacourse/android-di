@file:Suppress("NonAsciiCharacters")

package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.di.DependencyContainer
import woowacourse.shopping.ui.MainActivity

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @Test
    fun `Activity 실행 테스트`() {
        val activityController = Robolectric.buildActivity(MainActivity::class.java)

        try {
            val activity = activityController.setup().get()
            assertThat(activity).isNotNull()
        } finally {
            activityController.destroy()
            DependencyContainer.closeScope(ShoppingApplication.APP_SCOPE_ID)
        }
    }
}
