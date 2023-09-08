package woowacourse.shopping.di.constructordi

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.FakeRepository

class FakeConstructorDIViewModel(val fakeRepository: FakeRepository) : ViewModel()
