package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.RepositoryProvider
import woowacourse.shopping.data.Repository
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

object ViewModelFactoryProvider {
    fun <T : ViewModel> factory(viewModel: KClass<T>): ViewModelProvider.Factory {
        val parameters: Array<Repository> =
            viewModel.primaryConstructor
                ?.parameters
                ?.mapNotNull { parameter: KParameter ->
                    RepositoryProvider.repositories[(parameter.type.classifier as? KClass<*>)]
                }.orEmpty()
                .toTypedArray()

        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel.primaryConstructor?.call(*parameters) as T
        }
    }
}
