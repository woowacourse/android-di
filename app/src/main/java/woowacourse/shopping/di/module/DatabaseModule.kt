package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Provider
import woowacourse.shopping.di.definition.DefinitionInformation
import woowacourse.shopping.di.definition.Kind
import woowacourse.shopping.di.definition.Qualifier

class DatabaseModule(
    private val context: Context,
) : InjectionModule {
    override fun provideDefinitions(): List<DefinitionInformation<*>> =
        listOf(
            DefinitionInformation(
                kclass = ShoppingDatabase::class,
                qualifier = Qualifier.Named("room"),
                kind = Kind.SINGLETON,
                provider = { _ -> Provider { provideRoomDatabase() } },
            ),
            DefinitionInformation(
                kclass = ShoppingDatabase::class,
                qualifier = Qualifier.Named("memory"),
                kind = Kind.FACTORY,
                provider = { _ -> Provider { provideInMemoryDatabase() } },
            ),
            DefinitionInformation(
                kclass = CartProductDao::class,
                qualifier = null,
                kind = Kind.SINGLETON,
                provider = { injector ->
                    Provider {
                        val db = injector.get<ShoppingDatabase>(Qualifier.Named("memory"))
                        db.cartProductDao()
                    }
                },
            ),
        )

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
