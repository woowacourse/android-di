package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Provider
import woowacourse.shopping.di.definition.DefinitionInformation
import woowacourse.shopping.di.definition.Kind

class DatabaseModule : InjectionModule {
    override fun provideDefinitions(): List<DefinitionInformation<*>> =
        listOf(
            DefinitionInformation(
                kclass = ShoppingDatabase::class,
                qualifier = null,
                kind = Kind.SINGLETON,
                provider = { _ -> Provider { localDatabase } },
            ),
            DefinitionInformation(
                kclass = CartProductDao::class,
                qualifier = null,
                kind = Kind.SINGLETON,
                provider = { _ -> Provider { localDatabase.cartProductDao() } },
            ),
        )

    companion object {
        private const val DATABASE_NAME = "shopping_db"
        lateinit var localDatabase: ShoppingDatabase
            private set

        fun init(context: Context) {
            localDatabase =
                Room
                    .databaseBuilder(
                        context,
                        ShoppingDatabase::class.java,
                        DATABASE_NAME,
                    ).build()
        }
    }
}
