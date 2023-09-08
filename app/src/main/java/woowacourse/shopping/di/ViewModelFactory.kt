package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
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

    private fun injectProperties(viewModel: ViewModel) {
        val injects = viewModel::class.declaredMemberProperties.filter {
            it.hasAnnotation<Inject>()
        }

        injects.forEach { kProperty1 ->
            val obj = container.getInstance(kProperty1.returnType.jvmErasure)
            println(obj)
            if (kProperty1 is KMutableProperty<*>) {
                kProperty1.isAccessible = true

                println(kProperty1.returnType.jvmErasure)
                kProperty1.javaField?.set(viewModel, obj)
            }
        }
    }

    private fun mapModulesInConstructor(constructor: KFunction<Any>): List<Any?> {
        return constructor.parameters.map { param ->
            requireNotNull(container.getInstance(param.type.jvmErasure)) {
                "No matching same type in param | type : ${param.type}"
            }
        }
    }
}
