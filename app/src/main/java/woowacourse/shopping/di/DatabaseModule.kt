package woowacourse.shopping.di

import android.content.Context
import androidx.room.Room
import com.shopping.di.InjectContainer
import com.shopping.di.InjectionModule
import com.shopping.di.Provider
import com.shopping.di.definition.Qualifier
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class DatabaseModule(
    private val context: Context,
) : InjectionModule {
    override fun provideDefinitions(container: InjectContainer) {
        container.apply {
            registerSingleton<ShoppingDatabase>(Qualifier.Named("room")) {
                Provider { provideRoomDatabase() }
            }
            registerFactory<ShoppingDatabase>(Qualifier.Named("memory")) {
                Provider { provideInMemoryDatabase() }
            }
            registerSingleton<CartProductDao> { container ->
                Provider {
                    val db = container.get<ShoppingDatabase>(Qualifier.Named("memory"))
                    db.cartProductDao()
                }
            }
        }
    }

    private fun provideRoomDatabase(): ShoppingDatabase =
        Room
            .databaseBuilder(
                context,
                ShoppingDatabase::class.java,
                DATABASE_NAME,
            ).build()

    private fun provideInMemoryDatabase(): ShoppingDatabase =
        Room
            .inMemoryDatabaseBuilder(
                context,
                ShoppingDatabase::class.java,
            ).build()

    companion object {
        private const val DATABASE_NAME = "shopping_db"
    }
}
