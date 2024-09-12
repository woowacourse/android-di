package woowacourse.shopping.ui.injection.repository

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import woowacourse.shopping.ui.injection.Binder
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

interface RepositoryDI

@Binder(RepositoryDI::class)
class RepositoryBinder {
    init {
        validateReturnTypes()
    }

    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    fun provideCartRepository(): CartRepository = CartRepositoryImpl()

    private fun validateReturnTypes() {
        val annotation = this::class.findAnnotation<Binder>()
        annotation?.let {
            this::class.declaredFunctions.filter { it.visibility == KVisibility.PUBLIC }.forEach { function ->
                val returnTypeClassifier = function.returnType.classifier as? KClass<*>
                if (returnTypeClassifier == null || !returnTypeClassifier.isSubclassOf(it.returnType)) {
                    throw IllegalArgumentException(
                        "클래스 ${this::class.simpleName}의 함수 ${function.name}의 반환 타입은 ${it.returnType.simpleName}이어야 합니다.",
                    )
                }
            }
        }
    }
}
