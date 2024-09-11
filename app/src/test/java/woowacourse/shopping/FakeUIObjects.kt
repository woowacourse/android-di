package woowacourse.shopping

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.ui.util.ReflectiveViewModelFactory

class FakeViewModel(
    val fakeRepository: FakeRepository,
) : ViewModel()

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by viewModels {
        ReflectiveViewModelFactory()
    }
}
