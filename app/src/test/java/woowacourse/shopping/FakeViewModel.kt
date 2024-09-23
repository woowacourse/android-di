package woowacourse.shopping

import androidx.lifecycle.ViewModel
import com.woowacourse.di.InMemory
import com.woowacourse.di.Inject
import com.woowacourse.di.RoomDB
import com.woowacourse.di.Singleton
import com.woowacourse.di.ViewModelScope

// 성공 케이스(인터페이스 1, 구현체 1)
class FirstSuccessCaseViewModel : ViewModel() {
    @Inject
    @InMemory
    @Singleton
    lateinit var fakeProductRepository: FakeProductRepository
}

// 실패 케이스(인터페이스 1, 구현체 2)
class FirstFailureCaseViewModel : ViewModel() {
    @Inject
    @ViewModelScope
    lateinit var fakeDefaultCartRepository: FakeCartRepository
}

// 성공 케이스(인터페이스 1, 구현체 2)
class SecondSuccessCaseViewModel : ViewModel() {
    @Inject
    @RoomDB
    @ViewModelScope
    lateinit var fakeDefaultCartRepository: FakeCartRepository
}

// 성공 케이스(인터페이스 1, 구현체 2)
class ThirdSuccessCaseViewModel : ViewModel() {
    @Inject
    @InMemory
    @Singleton
    lateinit var fakeProductRepository: FakeProductRepository

    @Inject
    @RoomDB
    @ViewModelScope
    lateinit var fakeDefaultCartRepository: FakeCartRepository
}
