package woowacourse.shopping.data

import woowacourse.shopping.model.Product

// TODO: Step2 - CartProductDao를 참조하도록 변경
class DefaultCartRepository(
    private val dao: CartProductDao,
) : CartRepository {
    override suspend fun addCartProduct(product: Product) {
        dao.insert(CartProductEntity(product.name, product.price, product.imageUrl))
    }

    override suspend fun allCartProducts(): List<Product> = dao.getAll().toData()

    override suspend fun deleteCartProduct(id: Long) {
        dao.delete(id)
    }
}

fun List<CartProductEntity>.toData(): List<Product> =
    map {
        Product(it.name, it.price, it.imageUrl)
    }
