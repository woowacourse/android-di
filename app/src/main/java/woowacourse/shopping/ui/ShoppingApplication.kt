package woowacourse.shopping.ui

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.Injector
import woowacourse.shopping.model.Product

class ShoppingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        inject()
    }

    private fun inject() {
        val db = Room.databaseBuilder(
            applicationContext,
            ShoppingDatabase::class.java,
            ShoppingDatabase.name,
        ).build()

        val products = listOf(
            Product(
                name = "우테코 과자",
                price = 10_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/api/main/df6d76fb-925b-40f8-9d1c-f0920c3c697a.jpg?h=700&w=700",
            ),
            Product(
                name = "우테코 쥬스",
                price = 8_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/52dca718-31c5-4f80-bafa-7e300d8c876a.jpg?h=700&w=700",
            ),
            Product(
                name = "우테코 아이스크림",
                price = 20_000,
                imageUrl = "https://cdn-mart.baemin.com/sellergoods/main/e703c53e-5d01-4b20-bd33-85b5e778e73f.jpg?h=700&w=700",
            ),
        )

        Container.addInstance(List::class, products)
        Container.addInstance(CartProductDao::class, db.cartProductDao())

        Container.addInstance(ProductRepository::class, Injector.inject(DefaultProductRepository::class))
        Container.addInstance(CartRepository::class, Injector.inject(DefaultCartRepository::class))
    }
}
