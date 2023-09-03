package woowacourse.shopping.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.ui.RepositoryModules

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.declaredConstructors.first()
        require(constructor != null) { IllegalArgumentException("Unknown ViewModel Class $modelClass") }

        val types = constructor.parameterTypes
        val params = mutableListOf<Any?>()

        val applicationProperties = RepositoryModules::class.java.declaredFields
        for (type in types) {
            val field = applicationProperties.first { it.type == type }
                ?: throw IllegalArgumentException("Can't find Property $type")
            field.isAccessible = true
            params.add(field.get(null))
        }

        return constructor.newInstance(*params.toTypedArray()) as T
    }
}
