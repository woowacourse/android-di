package woowacourse.shopping.di

class PackageName private constructor() {
    companion object {
        const val PRODUCT = "woowacourse.shopping.data.DefaultProductRepository"
        const val DISPOSABLE_CART = "woowacourse.shopping.data.InMemoryCartRepository"
        const val DATABASE_CART = "woowacourse.shopping.data.DatabaseCartRepository"
    }
}