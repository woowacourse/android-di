package woowacourse.shopping.fixture

import androidx.lifecycle.ViewModel

class ConstructorTestViewModel : ViewModel()

class ConstructorTestViewModelWithDependency(
    val repository: FakeProductRepository,
) : ViewModel()

class ConstructorTestViewModelWithDefaultDependency(
    val repository: FakeProductRepository = FakeProductRepository(),
) : ViewModel()
