package woowacourse.shopping.module

import androidx.room.Room
import org.koin.dsl.module
import woowacourse.shopping.local.ShoppingDatabase

val databaseModule = module {
    // DB & DAO (local)
    single(createdAtStart = true) {
        Room.databaseBuilder(
            get(),
            ShoppingDatabase::class.java, ShoppingDatabase.NAME
        ).build()
    }
    single { get<ShoppingDatabase>().cartProductDao() }

}