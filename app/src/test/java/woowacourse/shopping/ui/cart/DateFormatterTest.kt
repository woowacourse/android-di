package woowacourse.shopping.ui.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class DateFormatterTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `입력한_시간이_3000ms면_1월 1970 09_00_03를_반환한다`() {
        // given
        val dateFormatter = DateFormatter(RuntimeEnvironment.getApplication())

        // when
        val timeStamp = 3000L
        val actual = dateFormatter.formatDate(timeStamp)

        // then
        val expect = "1 1월 1970 09:00:03"
        assertEquals(expect, actual)
    }
}
