package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory(private val container: Container) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = requireNotNull(modelClass.kotlin.primaryConstructor)
        val params = mapModulesInConstructor(constructor)
        return constructor.call(*params.toTypedArray()).apply { injectProperties(this) }
    }

    private fun mapModulesInConstructor(constructor: KFunction<Any>): List<Any?> {
        return constructor.parameters.map { param ->
            requireNotNull(container.getInstance(param.type.jvmErasure)) {
                "No matching same type in param | type : ${param.type}"
            }
        }
    }

    private fun injectProperties(viewModel: ViewModel) {
        val properties = viewModel::class.declaredMemberProperties.filter {
            it.hasAnnotation<Inject>()
        }
        properties.forEach { injectProperty(it, viewModel) }
    }

    private fun injectProperty(property: KProperty1<out ViewModel, *>, viewModel: ViewModel) {
        val obj = container.getInstance(property.returnType.jvmErasure)
        if (property is KMutableProperty<*>) {
            property.isAccessible = true
            property.javaField?.set(viewModel, obj)
        }
    }
}
