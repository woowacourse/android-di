package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import org.library.haeum.di.HaeumInject

class FakeViewModel3 : ViewModel() {
    @HaeumInject
    @FakeRoomDBRepository
    lateinit var fakeRepository1: FakeRepository

    @HaeumInject
    @FakeInMemoryRepository
    lateinit var fakeRepository2: FakeRepository
}
