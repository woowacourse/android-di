package woowacourse.shopping.fixture

import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass

class TestAppContainer {
    val fakeProductRepository = FakeProductRepository()
    val fakeCartRepository = FakeCartRepository()

    private val dependencies = mutableMapOf<Pair<KClass<*>, KClass<out Annotation>?>, Any>()

    init {
        addDependency(ProductRepository::class, fakeProductRepository)
        addDependency(CartRepository::class, fakeCartRepository)
        addDependency(FakeProductRepository::class, fakeProductRepository)
    }

    fun <T : Any> addDependency(
        type: KClass<T>,
        instance: T,
        qualifier: KClass<out Annotation>? = null,
    ) {
        dependencies[type to qualifier] = instance
    }

    fun resolve(type: KClass<*>): Any? {
        val candidates = dependencies.filterKeys { it.first == type }
        return when {
            candidates.size == 1 -> candidates.values.first()
            candidates.isEmpty() -> null
            else -> throw IllegalArgumentException("여러 구현체가 존재하지만 Qualifier가 없습니다: ${type.simpleName}")
        }
    }

    fun clear() = dependencies.clear()
}
