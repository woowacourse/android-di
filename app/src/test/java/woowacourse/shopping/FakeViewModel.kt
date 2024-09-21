package woowacourse.shopping

import androidx.lifecycle.ViewModel
import com.woowacourse.di.InMemory
import com.woowacourse.di.Inject
import com.woowacourse.di.RoomDB

// 성공 케이스(인터페이스 1, 구현체 1)
class FirstSuccessCaseViewModel : ViewModel() {
    @Inject
    @InMemory
    lateinit var fakeProductRepository: FakeProductRepository
}

// 실패 케이스(인터페이스 1, 구현체 2)
class FirstFailureCaseViewModel : ViewModel() {
    @Inject
    lateinit var fakeDefaultCartRepository: FakeCartRepository
}

// 성공 케이스(인터페이스 1, 구현체 2)
class SecondSuccessCaseViewModel : ViewModel() {
    @Inject
    @RoomDB
    lateinit var fakeDefaultCartRepository: FakeCartRepository
}

// 성공 케이스(인터페이스 1, 구현체 2)
class ThirdSuccessCaseViewModel : ViewModel() {
    @Inject
    @InMemory
    lateinit var fakeProductRepository: FakeProductRepository

    @Inject
    @RoomDB
    lateinit var fakeDefaultCartRepository: FakeCartRepository
}
