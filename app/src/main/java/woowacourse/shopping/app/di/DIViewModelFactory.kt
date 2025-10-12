package woowacourse.shopping.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.Inject
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

class DIViewModelFactory(
    private val appContainer: Container,
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
                    val qualifierAnnotation: Annotation? =
                        property.annotations.find {
                            it.annotationClass.annotations.any { meta ->
                                meta.annotationClass.simpleName == "Qualifier"
                            }
                        }
                    val dependency =
                        appContainer.resolve(requestedType, qualifierAnnotation)
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
