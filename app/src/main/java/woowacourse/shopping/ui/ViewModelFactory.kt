package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

class ViewModelFactory(
    private val repositoryMap: Map<KClass<*>, Any>,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.constructors.first()
        val params =
            constructor.parameters
                .map { param ->
                    repositoryMap[param.type.kotlin]
                        ?: throw IllegalArgumentException("No provider for ${param.type}")
                }.toTypedArray()
        return constructor.newInstance(*params) as T
    }
}
