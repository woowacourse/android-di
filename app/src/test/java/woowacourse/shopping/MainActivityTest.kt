package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.MainActivity

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .setup()
                .get()

        // then
        assertThat(activity).isNotNull()
    }
}
