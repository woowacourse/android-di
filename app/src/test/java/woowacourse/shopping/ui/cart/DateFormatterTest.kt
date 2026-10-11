package woowacourse.shopping.ui.cart

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Calendar
import java.util.GregorianCalendar

class DateFormatterTest {
    @Test
    fun `Android Context 없이 전달받은 날짜 형식으로 시간을 출력한다`() {
        val date = GregorianCalendar(2026, Calendar.JANUARY, 2, 3, 4, 5).timeInMillis
        val formatter = DateFormatter("yyyy-MM-dd HH:mm:ss")

        assertThat(formatter.formatDate(date)).isEqualTo("2026-01-02 03:04:05")
        assertThat(DateFormatter("yyyy/MM/dd").formatDate(date)).isEqualTo("2026/01/02")
    }
}
