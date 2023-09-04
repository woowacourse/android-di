package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.Constructor
import java.lang.reflect.Field

class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.declaredConstructors.first()
        val params = mapModulesInConstructor(
            constructor = constructor,
            modules = Module::class.java.declaredFields,
        )
        return constructor.newInstance(*params.toTypedArray()) as T
    }

    private fun mapModulesInConstructor(
        constructor: Constructor<*>,
        modules: Array<Field>,
    ): List<Any?> {
        return constructor.parameterTypes.map { parameterType ->
            val module = getFieldWithType(fields = modules, type = parameterType)
            module.isAccessible = true
            module.get(modules)
        }
    }

    private fun getFieldWithType(fields: Array<Field>, type: Class<*>): Field {
        val module = fields.firstOrNull { it.type.simpleName == type.simpleName }
        requireNotNull(module) { "No matching same type in fields | type : $type" }
        return module
    }
}
