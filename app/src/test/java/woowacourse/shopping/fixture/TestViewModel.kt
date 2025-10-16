package woowacourse.shopping.fixture

import androidx.lifecycle.ViewModel

class ConstructorTestViewModel : ViewModel()

class UnregisteredRepository

class ConstructorTestViewModelWithDependency(
    val repository: FakeProductRepository,
) : ViewModel()

class ConstructorTestViewModelWithDefaultDependency(
    val repository: FakeProductRepository = FakeProductRepository(),
) : ViewModel()

class ViewModelWithUnregisteredDependency(
    val repository: UnregisteredRepository,
) : ViewModel()
