package woowacourse.shopping.hilt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

//    @Test
//    fun `Activity 실행 테스트`() {
//        // given
//        val activity =
//            Robolectric
//                .buildActivity(MainActivity::class.java)
//                .create()
//                .get()
//
//        // then
//        activity.shouldNotBeNull()
//    }
//
//    @Test
//    fun `ViewModel 주입 테스트`() {
//        // given
//        val activity =
//            Robolectric
//                .buildActivity(MainActivity::class.java)
//                .create()
//                .get()
//        // when & then
//        shouldNotThrow<IllegalStateException> {
//            activity.getViewModel<MainViewModel>()
//        }
//    }
//
}
