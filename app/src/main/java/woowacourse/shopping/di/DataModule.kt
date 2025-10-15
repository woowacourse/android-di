package woowacourse.shopping.di

import androidx.room.Room
import com.daedan.compactAndroidDi.DiApplication
import com.daedan.compactAndroidDi.module
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
