package woowacourse.shopping.di.constructordi

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.FakeRepository
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.di.annotation.OnDisk

class FakeConstructorDIViewModel(
    @InMemory val inMemoryFakeRepository: FakeRepository,
    @OnDisk val onDiskFakeRepository: FakeRepository,
) : ViewModel()
