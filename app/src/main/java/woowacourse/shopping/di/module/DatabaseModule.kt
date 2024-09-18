package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import com.zzang.di.DIContainer
import com.zzang.di.annotation.QualifierType
import com.zzang.di.module.DIModule
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DatabaseModule(private val context: Context) : DIModule {
    override fun register(container: DIContainer) {
        val database =
            Room.databaseBuilder(
                context.applicationContext,
                ShoppingDatabase::class.java,
                "shopping_database",
            ).build()

        val cartProductDao = database.cartProductDao()

        container.registerInstance(CartProductDao::class, cartProductDao, QualifierType.DATABASE)

        container.registerInstance(ShoppingDatabase::class, database, QualifierType.DATABASE)
    }
}
