package woowacourse.shopping.util.autoDI.dependencyContainer

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.util.autoDI.AutoDI

private class FakeRepository

private class FakeViewModel(fakeRepository: FakeRepository) : ViewModel()

private class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by viewModels { AutoDiViewModelFactory.value }
}

@RunWith(RobolectricTestRunner::class)
class AutoDiViewModelFactoryTest {

    // 추후 테스트 독립성 확립해야함
    @Test
    fun `AutoDI 내에 주입 받으려는 뷰모델이 선언되어있다면 ViewModel 의존성 주입이 성공한다`() {
        // given
        AutoDI {
            singleton { FakeRepository() }
            viewModel { FakeViewModel(inject()) }
        }

        // when
        val activity: FakeActivity? = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()
        val viewModel = activity?.viewModel

        // then
        assertThat(viewModel).isNotNull()
        assertThat(viewModel).isInstanceOf(FakeViewModel::class.java)
    }
}
