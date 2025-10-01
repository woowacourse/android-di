package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.DiContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class AutoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor = requireNotNull(kClass.primaryConstructor) { kClass.java.simpleName }
        val params =
            constructor.parameters
                .associateWith { param ->
                    DiContainer.getInstance(param.type.classifier as KClass<*>)
                }
        return constructor.callBy(params)
    }
}
