package woowacourse.shopping.fake

import androidx.lifecycle.ViewModel
import org.library.haeum.Inject

class FakeViewModel : ViewModel() {

    @Inject
    @FakeRoomDBRepository
    lateinit var fakeRepository: FakeRepository
}