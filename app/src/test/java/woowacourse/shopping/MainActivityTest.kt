package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `메인 액티비티가 제거되면 주입되었던 DateFormatter 객체에 대한 참조도 사라진다`() {
        // given
        val activity =
            Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .get()

        // when


        // then

    }

    @Test
    fun `메인 액티비티가 회전되어도 DateFormatter 객체에 대한 참조는 사라지면 안된다`() {
        // given
        

        // when


        // then

    }






}
