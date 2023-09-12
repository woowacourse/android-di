package woowacourse.shopping

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.InjectField
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.getViewModel

object Fake {
    interface FakeRepository

    class DefaultFakeRepository : FakeRepository

    class FakeViewModel(val repository: FakeRepository) : ViewModel() {

        @InjectField
        lateinit var pingu: FakeRepository

        lateinit var otter: FakeRepository
    }

    class FakeActivity : AppCompatActivity() {
        val viewModel by getViewModel<FakeViewModel>(TestApplication.injector)
    }

    class TestApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            Container.addInstance(DefaultFakeRepository::class, null)
            injector = Injector(Container)
        }

        companion object {
            lateinit var injector: Injector
        }
    }
}
