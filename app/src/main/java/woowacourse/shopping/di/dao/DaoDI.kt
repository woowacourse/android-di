package woowacourse.shopping.di.dao

import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf

interface DaoDI

class DaoBinder(private val database: ShoppingDatabase) {
    init {
        require(
            validateReturnTypes(),
        ) {
            "모든 함수의 반환 타입은 RepositoryDI여야 합니다."
        }
    }

    fun provideCartProductDao(): CartProductDao = database.cartProductDao()

    private fun validateReturnTypes(): Boolean {
        return this::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC }
            .all { function ->
                val returnTypeClassifier = function.returnType.classifier as? KClass<*>
                returnTypeClassifier != null && returnTypeClassifier.isSubclassOf(DaoDI::class)
            }
    }
}
