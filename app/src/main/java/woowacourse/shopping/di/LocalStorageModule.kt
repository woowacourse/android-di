package woowacourse.shopping.di

import androidx.room.Room
import com.angrypig.autodi.autoDIModule.autoDIModule
import woowacourse.shopping.data.localStorage.CartProductDao
import woowacourse.shopping.data.localStorage.ShoppingDatabase
import woowacourse.shopping.data.localStorage.ShoppingDatabase.Companion.SHOPPING_DATABASE_NAME

val localStorageModule = autoDIModule {
    singleton<CartProductDao>("cartProductDao") {
        Room.databaseBuilder(
            injectApplicationContext(),
            ShoppingDatabase::class.java,
            SHOPPING_DATABASE_NAME,
        ).build().cartProductDao()
    }
}
