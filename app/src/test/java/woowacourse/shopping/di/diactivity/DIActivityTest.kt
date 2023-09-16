package woowacourse.shopping.di.diactivity

import androidx.lifecycle.ViewModel
import com.di.berdi.Container
import com.di.berdi.DIActivity
import com.di.berdi.DIApplication
import com.di.berdi.Injector
import com.di.berdi.annotation.InMemory
import com.di.berdi.annotation.Inject
import com.di.berdi.annotation.OnDisk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.FakeObj
import woowacourse.shopping.di.FakeRepository
import woowacourse.shopping.di.InMemoryFakeRepository
import woowacourse.shopping.di.OnDiskFakeRepository

@RunWith(RobolectricTestRunner::class)
class DIActivityTest {
    inner class FakeAnnotationDIApplication : DIApplication() {
        override fun inject() {
            val container = Container()
            injector =
                Injector(container, applicationContext).apply { inject(FakeObjModule) }
        }
    }

    @Test
    @Config(application = FakeAnnotationDIApplication::class)
    fun `어노테이션을 사용한 프로퍼티만 주입된다`() {
        class FakeAnnotationDIViewModel : ViewModel() {
            @Inject
            lateinit var fakeObj: FakeObj
        }

        class FakeAnnotationDIActivity : DIActivity() {
            lateinit var viewModel: FakeAnnotationDIViewModel
        }

        val activity = Robolectric
            .buildActivity(FakeAnnotationDIActivity::class.java)
            .create()
            .get()

        val viewModel = activity?.viewModel!!

        assertThat(viewModel.fakeObj).isSameAs(FakeObj)
    }

    inner class FakeConstructorDIApplication : DIApplication() {
        override fun inject() {
            val container = Container()
            injector = Injector(container, applicationContext).apply {
                inject(FakeRepositoryModule)
            }
        }
    }

    @Test
    @Config(application = FakeConstructorDIApplication::class)
    fun `맞는 타입의 객체 인스턴스가 존재하면 ViewModel 의존성 주입이 성공한다`() {
        class FakeConstructorDIViewModel(
            @InMemory val inMemoryFakeRepository: FakeRepository,
            @OnDisk val onDiskFakeRepository: FakeRepository,
        ) : ViewModel()

        class FakeConstructorDIActivity : DIActivity() {
            lateinit var viewModel: FakeConstructorDIViewModel
        }

        val activity = Robolectric
            .buildActivity(FakeConstructorDIActivity::class.java)
            .create()
            .get()

        val viewModel = activity?.viewModel

        assertThat(viewModel).isNotNull

        // and
        assertThat(viewModel?.inMemoryFakeRepository).isInstanceOf(InMemoryFakeRepository::class.java)
        assertThat(viewModel?.onDiskFakeRepository).isInstanceOf(OnDiskFakeRepository::class.java)
    }
}
