package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

class DIViewModelFactory(
    private val appContainer: AppContainer,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val viewModelKClass = modelClass.kotlin

        val constructor: KFunction<*> =
            viewModelKClass.constructors.find { it.parameters.isEmpty() }
                ?: throw IllegalArgumentException(
                    "${viewModelKClass.qualifiedName} 클래스는 필드 주입을 위해 기본 생성자를 선언해야 합니다.",
                )

        val instance = constructor.call() as T

        viewModelKClass.memberProperties
            .filter { property ->
                property.annotations.any { it.annotationClass == Inject::class }
            }.forEach { property ->
                if (property is KMutableProperty1<*, *>) {
                    val requestedType: KType = property.returnType

                    val dependency =
                        appContainer.resolve(requestedType)
                            ?: throw IllegalStateException(
                                "필드 타입 ${requestedType}에 대한 제공자가 AppContainer에 없습니다.",
                            )

                    property.setter.call(instance, dependency)
                } else {
                    throw IllegalStateException(
                        "${property.name} 필드는 필드 주입을 위해 lateinit var로 선언해야 합니다.",
                    )
                }
            }

        return instance
    }
}

fun AppContainer.resolve(requestedType: KType): Any? {
    val kClass = requestedType.classifier as? KClass<*> ?: return null
    return when (kClass) {
        ProductRepository::class, DefaultProductRepository::class -> productRepository
        CartRepository::class, DefaultCartRepository::class -> cartRepository
        else -> null
    }
}
