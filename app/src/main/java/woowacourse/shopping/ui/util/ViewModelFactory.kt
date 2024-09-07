package woowacourse.shopping.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import woowacourse.shopping.di.InstanceContainer

class ViewModelFactory(
    private val viewModelClass: KClass<out ViewModel>,
    private val instanceContainer: InstanceContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(viewModelClass.java)) { EXCEPTION_VIEWMODEL_NOT_FOUND }

        val primaryConstructor = viewModelClass.primaryConstructor
        requireNotNull(primaryConstructor) { EXCEPTION_NO_PRIMARY_CONSTRUCTOR.format(viewModelClass.simpleName) }

        val primaryConstructorParameters = primaryConstructor.parameters
        val instance = primaryConstructor.callBy(
            primaryConstructorParameters.associateWith { parameter ->
                instanceContainer.instanceOf(parameter.type)
            }
        )

        return instance as T
    }

    companion object {
        private const val EXCEPTION_VIEWMODEL_NOT_FOUND = "ViewModel class not found"
        private const val EXCEPTION_NO_PRIMARY_CONSTRUCTOR = "No primary constructor found for %s"
    }
}

fun <VM : ViewModel> viewModelFactory(creator: () -> VM): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return creator() as T
        }
    }
}
