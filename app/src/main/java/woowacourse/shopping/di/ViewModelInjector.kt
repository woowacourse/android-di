package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.RepositoryModule
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ViewModelInjector(private val repositoryModule: RepositoryModule) {
    fun <T : ViewModel> inject(modelClass: KClass<T>): T {
        val constructors = requireNotNull(modelClass.primaryConstructor) { "No suitable constructor found for $modelClass" }
        val parameters = constructors.parameters.map { it.type.classifier as KClass<*> }
        val arguments = parameters.map { repositoryModule.getRepository(it) }
        return constructors.call(*arguments.toTypedArray())
    }
}
