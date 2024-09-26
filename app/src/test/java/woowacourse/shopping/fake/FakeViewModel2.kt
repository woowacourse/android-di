package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import org.library.haeum.di.HaeumInject

class FakeViewModel2 : ViewModel() {
    @HaeumInject
    @FakeInMemoryRepository
    lateinit var fakeRepository: FakeRepository
}
