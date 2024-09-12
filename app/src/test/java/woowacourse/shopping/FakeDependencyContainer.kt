package woowacourse.shopping

import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

class FakeDependencyContainer : DependencyContainer {
    private val dependencies: MutableMap<Pair<KClassifier, String>, KClass<*>> =
        mutableMapOf()

    private val cachedInstances: MutableMap<Pair<KClassifier, String>, Any> =
        mutableMapOf()

    init {
        setInstance(
            ProductRepository::class,
            FakeProductRepository(fakeProducts),
            "",
        )

        setInstance(
            CartRepository::class,
            FakeCartRepository(fakeCartProducts.toMutableList()),
            "",
        )
    }

    override fun <T : Any> getInstance(
        kClassifier: KClassifier,
        qualifier: String,
    ): T? {
        val qualifierKey = qualifier.ifEmpty { "" }
        return cachedInstances[kClassifier to qualifierKey] as T?
    }

    override fun <T : Any> getImplement(
        kClassifier: KClassifier,
        qualifier: String,
    ): KClass<T>? {
        val qualifierKey = qualifier.ifEmpty { "" }
        return dependencies[kClassifier to qualifierKey] as KClass<T>?
    }

    override fun <T : Any> setDependency(
        kClassifier: KClassifier,
        kClass: KClass<T>,
        qualifier: String,
    ) {
        val qualifierKey = qualifier.ifEmpty { "" }
        dependencies[kClassifier to qualifierKey] = kClass
    }

    override fun setInstance(
        kClassifier: KClassifier,
        instance: Any,
        qualifier: String,
    ) {
        val qualifierKey = qualifier.ifEmpty { "" }
        cachedInstances[kClassifier to qualifierKey] = instance
    }
}

val fakeDependencyContainer: DependencyContainer = FakeDependencyContainer()
