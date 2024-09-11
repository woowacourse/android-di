package woowacourse.shopping

import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.ui.util.DependencyContainer
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

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
        setInstance(
            CartRepository::class,
            FakeCartRepository(fakeCartProducts.toMutableList()),
        )
        setInstance(
            ProductRepository::class,
            FakeProductRepository(fakeProducts),
        )
    }

    override fun <T : Any> getInstance(kClassifier: KClassifier): T? {
        return cachedInstances[kClassifier] as T?
    }

    override fun <T : Any> getImplement(kClassifier: KClassifier): KClass<T>? {
        return dependencies[kClassifier] as KClass<T>?
    }

    override fun <T : Any> setDependency(kClassifier: KClassifier, kClass: KClass<T>) {
        dependencies[kClassifier] = kClass
    }

    override fun setInstance(kClassifier: KClassifier, instance: Any) {
        cachedInstances[kClassifier] = instance
    }
}

val fakeDependencyContainer: DependencyContainer = FakeDependencyContainer()
