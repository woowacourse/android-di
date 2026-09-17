package woowacourse.shopping.data

import kotlin.reflect.KClass

class RepositoryContainer(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
) {
    private val repositoryMap = mapOf(
        ProductRepository::class to productRepository,
        CartRepository::class to cartRepository,
    )

    fun getInstance(type: KClass<*>): Any =
        repositoryMap[type] ?: throw IllegalArgumentException("존재하지 않는 타입: $type")
}