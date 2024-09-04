package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val dependencies: MutableMap<Class<out Any>, Any>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val constructor = modelClass.constructors.firstOrNull()
            ?: throw IllegalArgumentException("No primary constructor")

        val params = constructor.parameters.map { param ->
            dependencies[param.type] ?: throw IllegalArgumentException(
                "Dependency not found for parameter type"
            )
        }
        return constructor.newInstance(*params.toTypedArray()) as T
    }
}
