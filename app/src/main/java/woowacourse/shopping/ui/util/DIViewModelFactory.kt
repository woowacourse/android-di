package woowacourse.shopping.ui.util

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import woowacourse.shopping.di.AppContainer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIViewModelFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        val constructor =
            requireNotNull(modelClass.kotlin.primaryConstructor) {
                "${modelClass.simpleName}에 Public 생성자가 없습니다."
            }

        return constructor.parameters
            .associateWith { param ->
                resolveConstructorParam(param.type.classifier as? KClass<*>, handle)
            }.let(constructor::callBy)
    }

    private fun resolveConstructorParam(
        paramClass: KClass<*>?,
        handle: SavedStateHandle,
    ): Any =
        when (paramClass) {
            null -> error("타입 정보를 알 수 없습니다.")
            SavedStateHandle::class -> handle
            else -> AppContainer.resolve(paramClass)
        }
}
