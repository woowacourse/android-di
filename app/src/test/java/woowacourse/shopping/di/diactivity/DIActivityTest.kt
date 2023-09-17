package woowacourse.shopping.di.diactivity

import androidx.lifecycle.ViewModel
import com.di.berdi.Container
import com.di.berdi.DIActivity
import com.di.berdi.DIApplication
import com.di.berdi.Injector
import com.di.berdi.annotation.Inject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.FakeObj
import woowacourse.shopping.di.FakeRepository
import woowacourse.shopping.di.InMemory
import woowacourse.shopping.di.InMemoryFakeRepository
import woowacourse.shopping.di.OnDisk
import woowacourse.shopping.di.OnDiskFakeRepository

class FakeDIApplication : DIApplication() {
    override fun inject() {
        injector =
            Injector(Container(), applicationContext).apply {
                injectBy(
                    listOf(FakeRepositoryModule, FakeObjModule),
                )
            }
    }
}

class FakeDIViewModel(
    @InMemory val inMemoryFakeRepository: FakeRepository,
    @OnDisk val onDiskFakeRepository: FakeRepository,
) : ViewModel() {
    @Inject
    lateinit var fakeObj: FakeObj
}

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeDIApplication::class)
class DIActivityTest {

    @Test
    fun `맞는 타입의 객체 인스턴스가 존재하면 ViewModel 의존성 주입이 성공한다`() {
        class FakeDIActivity : DIActivity() {
            lateinit var viewModel: FakeDIViewModel
        }

        val activity = Robolectric
            .buildActivity(FakeDIActivity::class.java)
            .create()
            .get()

        val viewModel = activity?.viewModel

        assertThat(viewModel).isNotNull

        // and
        assertThat(viewModel?.inMemoryFakeRepository).isInstanceOf(InMemoryFakeRepository::class.java)
        assertThat(viewModel?.onDiskFakeRepository).isInstanceOf(OnDiskFakeRepository::class.java)

        // and: field inject
        assertThat(viewModel?.fakeObj).isSameAs(FakeObj)
    }
}
