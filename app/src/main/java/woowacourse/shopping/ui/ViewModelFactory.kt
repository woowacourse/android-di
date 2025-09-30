package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.RepositoryProvider
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        val constructor: KFunction<VM>? = modelClass.kotlin.primaryConstructor
        val parameters =
            constructor
                ?.parameters
                ?.mapNotNull { parameter: KParameter ->
                    RepositoryProvider.Repositories[(parameter.type.classifier as KClass<*>)]
                }.orEmpty()
                .toTypedArray()
        return constructor?.call(*parameters) as VM
    }
}
