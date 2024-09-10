package woowacourse.shopping

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import woowacourse.shopping.domain.model.Product
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.util.DependencyContainer
import woowacourse.shopping.ui.util.DependencyInjector
import woowacourse.shopping.ui.util.ReflectiveViewModelFactory
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.createInstance

class FakeCartRepository(
    private val cartProducts: MutableList<Product>,
) : CartRepository {
    override fun addCartProduct(product: Product) {
        cartProducts.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return cartProducts.toList()
    }

    override fun deleteCartProduct(id: Int) {
        cartProducts.removeAt(id)
    }
}

class FakeProductRepository(
    private val products: List<Product>,
) : ProductRepository {
    override fun getAllProducts(): List<Product> {
        return products
    }
}

val fakeProducts: MutableList<Product> =
    mutableListOf(
        Product(
            name = "우테코 김치",
            price = 5_900,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700"
        ),
        Product(
            name = "우테코 생수",
            price = 2_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700"
        ),
        Product(
            name = "우테코 삼겹살",
            price = 20_000,
            imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700"
        ),
    )

interface FakeRepository

class FakeRepositoryImpl : FakeRepository

class FakeViewModel(
    val fakeRepository: FakeRepository,
) : ViewModel()

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by viewModels {
        ReflectiveViewModelFactory(DependencyInjector(fakeDependencyContainer))
    }
}

class FakeDependencyContainer : DependencyContainer {
    private val dependencies: MutableMap<KClassifier, KClass<*>> =
        mutableMapOf()

    private val cachedInstances: MutableMap<KClassifier, Any> =
        mutableMapOf()

    init {
        setDependency(
            FakeRepository::class,
            FakeRepositoryImpl::class,
        )
    }

    override fun <T : Any> getInstance(kClassifier: KClassifier): T {
        return cachedInstances.getOrPut(kClassifier) {
            val kClass = dependencies[kClassifier]
                ?: throw IllegalArgumentException("$kClassifier : 알 수 없는 클래스 지정자 입니다.")
            kClass.createInstance()
        } as T
    }

    override fun <T : Any> setDependency(kClassifier: KClassifier, kClass: KClass<T>) {
        dependencies[kClassifier] = kClass
    }
}

val fakeDependencyContainer: DependencyContainer = FakeDependencyContainer()
