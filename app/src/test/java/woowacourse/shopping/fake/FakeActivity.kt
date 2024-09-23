package woowacourse.shopping.fake

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by viewModels()

    val viewModel2: FakeViewModel2 by viewModels()

    val viewModel3: FakeViewModel3 by viewModels()
}
