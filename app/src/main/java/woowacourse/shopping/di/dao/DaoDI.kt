package woowacourse.shopping.di.dao

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import javax.inject.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf

interface DaoDI

@Qualifier
annotation class InMemory

@Qualifier
annotation class Database

class DaoBinder(private val context: Context) {
    init {
        require(
            validateReturnTypes(),
        ) {
            "모든 함수의 반환 타입은 DaoDI여야 합니다."
        }
    }

    @Database
    fun provideCartProductDao(): CartProductDao = provideShoppingDataBase().cartProductDao()

    @InMemory
    fun provideInMemoryCartProductDao(): CartProductDao = provideShoppingInMemoryDataBase().cartProductDao()

    private fun provideShoppingDataBase(): ShoppingDatabase =
        Room.databaseBuilder(context, ShoppingDatabase::class.java, "shopping").build()

    private fun provideShoppingInMemoryDataBase(): ShoppingDatabase =
        Room.inMemoryDatabaseBuilder(context, ShoppingDatabase::class.java).build()

    private fun validateReturnTypes(): Boolean {
        return this::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC }
            .all { function ->
                val returnTypeClassifier = function.returnType.classifier as? KClass<*>
                returnTypeClassifier != null && returnTypeClassifier.isSubclassOf(DaoDI::class)
            }
    }
}
