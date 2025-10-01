package woowacourse.shopping.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.constructors.first()

        val params =
            constructor.parameterTypes
                .map { paramType: Class<*> ->
                    AppInjectionModule.getOrCreate(paramType)
                }.toTypedArray()

        return modelClass.cast(constructor.newInstance(*params)) as T
    }
}
