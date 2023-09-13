package woowacourse.shopping

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ui.common.getViewModel

interface FakeRepository2

class FakeViewModel2(
    val fakeRepository2: FakeRepository2,
) : ViewModel()

class FakeActivity2 : AppCompatActivity() {
    val viewModel by getViewModel<FakeViewModel2>(Fake.TestApplication.injector)
}

@RunWith(RobolectricTestRunner::class)
@Config(application = Fake.TestApplication::class)
class ViewModelInjectorTest {

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val activity = Robolectric
            .buildActivity(Fake.FakeActivity::class.java)
            .create()
            .get()

        assertNotNull(activity)

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel)
        assertEquals(Fake.DefaultFakeRepository::class, viewModel.repository::class)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivity2::class.java)
            .create()
            .get()
        activity.viewModel
    }
}
