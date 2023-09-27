package woowacourse.shopping.di

class PackageName private constructor() {
    companion object {
        const val PRODUCT = "woowacourse.shopping.data.DefaultProductRepository"
        const val DISPOSABLE_CART = "woowacourse.shopping.data.InMemoryCartRepository"
        const val DATABASE_CART = "woowacourse.shopping.data.DatabaseCartRepository"
    }
}

class ActivityClassName private constructor() {
    companion object {
        const val MAIN_ACTIVITY = "MainActivity"
        const val CART_ACTIVITY = "CartActivity"
    }
}
