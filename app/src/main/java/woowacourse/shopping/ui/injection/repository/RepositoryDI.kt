package woowacourse.shopping.ui.injection.repository

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import woowacourse.shopping.ui.injection.DIInjection
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.isSubclassOf

interface RepositoryDI : DIInjection

class RepositoryBinder {
    init {
        require(
            this::class.declaredFunctions.all { function ->
                val returnTypeClassifier = function.returnType.classifier as? KClass<*>
                returnTypeClassifier != null && returnTypeClassifier.isSubclassOf(RepositoryDI::class)
            },
        ) {
            "모든 함수의 반환 타입은 RepositoryDI여야 합니다."
        }
    }

    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    fun provideCartRepository(): CartRepository = CartRepositoryImpl()
}
