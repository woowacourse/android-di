package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

object DIViewModelFactory : ViewModelProvider.Factory by DependencyViewModelFactory(
    mapOf(
        ProductRepository::class to ProductRepository(),
        CartRepository::class to CartRepository(),
    ),
)

internal class DependencyViewModelFactory(
    private val dependencies: Map<KClass<*>, Any>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor ?: error("ViewModel의 주 생성자를 찾을 수 없습니다: ${modelClass.name}")
        val arguments =
            constructor.parameters.associateWith { parameter ->
                dependencies[parameter.type.jvmErasure]
                    ?: error("필요한 의존성을 찾을 수 없습니다: ${parameter.type} (${modelClass.name})")
            }
        return constructor.callBy(arguments)
    }
}
