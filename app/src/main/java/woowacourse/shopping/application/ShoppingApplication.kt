package woowacourse.shopping.application

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.container.DefaultContainer
import woowacourse.shopping.di.container.ShoppingContainer
import woowacourse.shopping.di.injector.Injector

class ShoppingApplication : Application() {
    lateinit var injector: Injector
        private set

    override fun onCreate() {
        super.onCreate()

        val db: ShoppingDatabase = createRoomDatabase()
        val appContainer: ShoppingContainer = DefaultContainer(db)
        injector = Injector(appContainer)
    }

    private fun createRoomDatabase(): ShoppingDatabase {
        return Room.databaseBuilder(
            this,
            ShoppingDatabase::class.java,
            "cart_products",
        ).build()
    }
}
