package woowacourse.shopping.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.DiContainer
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class DiViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val kClass = modelClass.kotlin

        val instances: Map<KParameter, Any>? =
            kClass.primaryConstructor?.parameters?.associateWith { it: KParameter ->
                DiContainer.getInstance(it.type.classifier as KClass<*>)
            }

        return kClass.primaryConstructor?.callBy(instances ?: error("")) as T
    }
}
