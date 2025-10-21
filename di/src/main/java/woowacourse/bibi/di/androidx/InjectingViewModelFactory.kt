package woowacourse.bibi.di.androidx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import woowacourse.bibi.di.core.Container
import woowacourse.bibi.di.core.MemberInjector
import woowacourse.bibi.di.core.Qualifier
import woowacourse.bibi.di.core.ViewModelScope
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

@Suppress("UNCHECKED_CAST")
class InjectingViewModelFactory(
    private val activityContainer: Container,
    private val owner: ViewModelStoreOwner,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val key = "${modelClass.name}.scope#${UUID.randomUUID()}"
        val holder = ViewModelProvider(owner).get(key, ViewModelScopeHolder::class.java)

        val viewModelContainer =
            holder.getOrCreate {
                activityContainer.child(ViewModelScope::class)
            }

        val kClass: KClass<out ViewModel> = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: kClass.constructors.maxByOrNull { it.parameters.size }
                ?: error("생성자를 찾을 수 없습니다: ${kClass.qualifiedName}")

        constructor.isAccessible = true

        val args =
            constructor.parameters
                .map { parameter ->
                    val qualifier =
                        parameter.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }?.annotationClass
                    viewModelContainer.resolve(parameter.type, qualifier)
                }.toTypedArray()

        val instance = constructor.call(*args) as T
        MemberInjector.inject(instance, viewModelContainer)
        return instance
    }
}
