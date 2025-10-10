package woowacourse.shopping.fixture

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.annotation.Inject
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

interface FixtureCar

data class ElectricFixtureCarImpl(
    val name: String,
) : FixtureCar

data class EngineFixtureCarImpl(
    val name: String,
) : FixtureCar

data class SingletonFixtureCar(
    val name: String,
)

data class FactoryFixtureCar(
    val name: String,
)

class ViewModelFieldInjectFixture : ViewModel() {
    @Inject
    lateinit var productRepository: ProductRepository

    @Inject
    lateinit var cartRepository: CartRepository
}

class ViewModelConstructorInjectFixture
    @Inject
    constructor(
        val productRepository: ProductRepository,
        val cartRepository: CartRepository,
    ) : ViewModel()
