package woowacourse.shopping.ui.util

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import woowacourse.shopping.di.AppContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras,
    ): T {
        val kClass = modelClass.kotlin
        val constructor =
            requireNotNull(kClass.primaryConstructor) {
                NO_PUBLIC_CONSTRUCTOR_ERROR_MESSAGE.format(kClass.simpleName ?: "Anonymous")
            }

        return constructor.parameters
            .associateWith { param ->
                resolveDependency(param.type.classifier as KClass<*>, extras)
            }.let(constructor::callBy)
    }

    private fun resolveDependency(
        clazz: KClass<*>,
        extras: CreationExtras,
    ): Any =
        when (clazz) {
            SavedStateHandle::class -> extras.createSavedStateHandle()
            else -> AppContainer.resolve(clazz)
        }

    private companion object {
        const val NO_PUBLIC_CONSTRUCTOR_ERROR_MESSAGE = "%s에 public 기본 생성자가 없습니다."
    }
}
