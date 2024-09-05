package woowacourse.shopping.ui.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.Repository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

object AutoDIManager {
    private val repositories: MutableMap<String, Any> = mutableMapOf()

    init {
        registerRepository(ProductRepositoryImpl)
        registerRepository(CartRepositoryImpl)
    }

    private fun registerRepository(repository: Any) {
        repositories[repository::class.typeParameters.toString()] = repository
    }

    private fun <T : Any> createInstanceWithParameters(
        clazz: KClass<T>,
        typeInputs: Map<KClass<*>, Any?>
    ): T? {
        val constructor = clazz.primaryConstructor ?: return null

        // 파라미터 타입에 맞는 값을 매핑합니다.
        val args = constructor.parameters.associateWith { parameter ->
            typeInputs[parameter.type.classifier as? KClass<*>]
        }
        constructor.isAccessible = true
        return constructor.callBy(args)
    }

    fun <T : ViewModel> createViewModelFactory(viewModelClass: KClass<T>): GenericViewModelFactory<ViewModel> {
        val args: Map<KClass<*>, Repository> = mapOf(
            ProductRepository::class to ProductRepositoryImpl,
            CartRepository::class to CartRepositoryImpl,
        )

        return GenericViewModelFactory(viewModelClass) {
            createInstanceWithParameters(viewModelClass, args) ?: error("Failed to create ViewModel instance")
        }
    }
}
