package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.bibi_di.AppContainer
import woowacourse.bibi_di.Inject
import woowacourse.bibi_di.Qualifier
import kotlin.jvm.java
import kotlin.reflect.KClass
import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

@Suppress("UNCHECKED_CAST")
class InjectingViewModelFactory(
    private val container: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass: KClass<out ViewModel> = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: kClass.constructors.maxByOrNull { it.parameters.size }
                ?: error("생성자를 찾을 수 없습니다: ${kClass.qualifiedName}")

        val args =
            constructor.parameters
                .map { parameter ->
                    val qualifier =
                        parameter.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }?.annotationClass
                    container.resolve(parameter.type, qualifier)
                }.toTypedArray()

        val instance = constructor.call(*args) as T
        injectAnnotatedFields(instance)
        return instance
    }

    private fun injectAnnotatedFields(target: Any) {
        target::class.java.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                val kType = field.type.kotlin.createType()
                val qualifier =
                    field.annotations
                        .firstOrNull {
                            it.annotationClass.hasAnnotation<Qualifier>()
                        }?.annotationClass
                val value = container.resolve(kType, qualifier)
                field.set(target, value)
            }
    }
}
