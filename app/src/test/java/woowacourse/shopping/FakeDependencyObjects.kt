package woowacourse.shopping

import woowacourse.shopping.ui.util.DependencyContainer
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier
import kotlin.reflect.full.createInstance

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
