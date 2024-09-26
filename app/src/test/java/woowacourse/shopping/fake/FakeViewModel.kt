package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import org.library.haeum.di.HaeumInject

class FakeViewModel : ViewModel() {
    @HaeumInject
    @FakeRoomDBRepository
    lateinit var fakeRepository: FakeRepository
}
