package woowacourse.shopping.fake

import androidx.appcompat.app.AppCompatActivity
import org.library.haeum.createViewModel

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by lazy {
        createViewModel()
    }

    val viewModel2: FakeViewModel2 by lazy {
        createViewModel()
    }

    val viewModel3: FakeViewModel3 by lazy {
        createViewModel()
    }
}