package woowacourse.bibi.di.androidx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.bibi.di.core.Container
import woowacourse.bibi.di.core.MemberInjector
import woowacourse.bibi.di.core.Qualifier
import woowacourse.bibi.di.core.ViewModelScope
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

@Suppress("UNCHECKED_CAST")
class InjectingViewModelFactory(
    private val container: Container,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelContainer = container.child(ViewModelScope::class)

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
                    viewModelContainer.resolve(parameter.type, qualifier)
                }.toTypedArray()

        val instance = constructor.call(*args) as T
        MemberInjector.inject(instance, container)
        return instance
    }
}
