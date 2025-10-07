package woowacourse.shopping.fixture

import kotlin.reflect.KClass

class TestAppContainer {
    val fakeRepository = FakeProductRepository()

    private val providers = mutableMapOf<KClass<*>, Any>()

    init {
        providers[FakeProductRepository::class] = fakeRepository
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> resolve(clazz: KClass<T>): T =
        providers[clazz] as? T
            ?: throw IllegalArgumentException("${clazz.simpleName} provider 없음")
}
