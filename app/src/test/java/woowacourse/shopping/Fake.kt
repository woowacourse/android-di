package woowacourse.shopping

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.pingudi.Container
import com.example.pingudi.Injector
import com.example.pingudi.annotation.InjectField
import woowacourse.shopping.ui.common.getViewModel

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
