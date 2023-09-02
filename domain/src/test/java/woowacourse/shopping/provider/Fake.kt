package woowacourse.shopping.provider

import woowacourse.shopping.model.Product

internal object Fake {
    fun ProductRepository(): woowacourse.shopping.repository.ProductRepository {
        return FakeProductRepository()
    }

    fun CartRepository(): woowacourse.shopping.repository.CartRepository {
        return FakeCartRepository()
    }
}

private class FakeProductRepository : woowacourse.shopping.repository.ProductRepository {
    private val products: MutableList<Product> = mutableListOf()

    override fun getAllProducts(): List<Product> {
        return products.toList()
    }
}

private class FakeCartRepository : woowacourse.shopping.repository.CartRepository {
    private val products: MutableList<Product> = mutableListOf()

    override fun addCartProduct(product: Product) {
        products.add(product)
    }

    override fun getAllCartProducts(): List<Product> {
        return products.toList()
    }

    override fun deleteCartProduct(id: Int) {
        products.removeAt(id)
    }
}
