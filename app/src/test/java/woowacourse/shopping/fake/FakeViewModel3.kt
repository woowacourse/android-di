package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import org.library.haeum.Inject

class FakeViewModel3 : ViewModel() {
    @Inject
    @FakeRoomDBRepository
    lateinit var fakeRepository1: FakeRepository

    @Inject
    @FakeInMemoryRepository
    lateinit var fakeRepository2: FakeRepository
}