package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import org.library.haeum.Inject

class FakeViewModel2 : ViewModel() {

    @Inject
    @FakeInMemoryRepository
    lateinit var fakeRepository: FakeRepository
}