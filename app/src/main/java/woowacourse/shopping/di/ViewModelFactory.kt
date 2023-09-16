package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class ViewModelFactory(private val container: Container) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = requireNotNull(modelClass.kotlin.primaryConstructor)
        val params = mapModulesInConstructor(constructor)
        return constructor.call(*params.toTypedArray())
    }

    private fun mapModulesInConstructor(constructor: KFunction<Any>): List<Any?> {
        return constructor.parameters.map { param ->
            requireNotNull(container.getInstance(param.type.jvmErasure)) {
                "No matching same type in param | type : ${param.type}"
            }
        }
    }
}
