package woowacourse.shopping.di.constructordi

import androidx.lifecycle.ViewModel
import com.di.berdi.annotation.InMemory
import com.di.berdi.annotation.OnDisk
import woowacourse.shopping.di.FakeRepository

class FakeConstructorDIViewModel(
    @InMemory val inMemoryFakeRepository: FakeRepository,
    @OnDisk val onDiskFakeRepository: FakeRepository,
) : ViewModel()
