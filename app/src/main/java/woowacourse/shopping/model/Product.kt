package woowacourse.shopping.model

import java.time.LocalDate

class Product(val name: String, val price: Int, val imageUrl: String)
class CartProduct(val name: String, val price: Int, val imageUrl: String, val createdAt: Long)