package woowacourse.shopping.di

import androidx.room.Room
import com.daedan.di.DiApplication
import com.daedan.di.module
import woowacourse.shopping.data.ShoppingDatabase

fun DiApplication.dataModule() =
    module {
        single {
            Room
                .databaseBuilder(
                    applicationContext,
                    ShoppingDatabase::class.java,
                    "shopping_db",
                ).build()
        }
        single { get<ShoppingDatabase>().cartProductDao() }
    }
