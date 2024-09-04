package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.AppContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(private val appContainer: AppContainer) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val constructors = requireNotNull(modelClass.primaryConstructor) { "No suitable constructor found for $modelClass" }
        val parameters = constructors.parameters.map { it.type.classifier as KClass<*> }
        val arguments = parameters.map { appContainer.getRepository(it) }
        return constructors.call(*arguments.toTypedArray())
    }
}
