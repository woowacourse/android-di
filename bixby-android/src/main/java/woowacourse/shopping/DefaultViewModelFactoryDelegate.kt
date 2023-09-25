package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class DefaultViewModelFactoryDelegate : ViewModelFactoryDelegate {
    private val injector = Injector.getSingletonInstance()

    fun <T : ViewModel> injectViewModel(target: KClass<T>): T {
        val constructor = requireNotNull(target.primaryConstructor) { "[ERROR] 주 생성자가 없습니다" }
        val params = constructor.parameters.map {
            Injector.getSingletonInstance().inject(it.type.jvmErasure)
        }
        return constructor.call(*params.toTypedArray()).apply { injectProperties(this) }
    }

    private fun <T : ViewModel> injectProperties(vm: T) {
        vm::class.declaredMemberProperties.forEach { property: KProperty1<out T, *> ->
            if (!property.hasAnnotation<Inject>()) return@forEach
            val qualifierTag = property.findAnnotation<Qualifier>()?.className
            property.isAccessible = true
            property.javaField?.set(
                vm,
                injector.inject(property.returnType.jvmErasure, qualifierTag),
            )
        }
    }

    override val Factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return injectViewModel(modelClass.kotlin)
        }
    }
}
