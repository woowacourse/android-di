package woowacourse.shopping.domain

// TODO: Step2 - CartProductDao를 참조하도록 변경
interface CartRepository {
    fun addCartProduct(product: Product)

    fun getAllCartProducts(): List<Product>

    fun deleteCartProduct(id: Int)
}
