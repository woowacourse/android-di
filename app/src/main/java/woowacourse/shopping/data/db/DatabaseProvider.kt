package woowacourse.shopping.data.db

interface DatabaseProvider {
    val cartDao: CartProductDao
}
