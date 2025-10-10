package woowacourse.shopping.fixture

import kotlin.reflect.KClass

class TestAppContainer {
    val fakeProductRepository = FakeProductRepository()

    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        providers[FakeProductRepository::class] = fakeProductRepository
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: KClass<T>): T =
        providers[clazz] as? T
            ?: throw IllegalArgumentException("${clazz.simpleName} provider 없음")
}
