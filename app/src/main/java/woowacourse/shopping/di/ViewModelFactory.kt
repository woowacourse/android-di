package woowacourse.shopping.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val container: AppContainer,
    private val owner: SavedStateRegistryOwner,
    private val defaultArgs: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("No primary constructor found for $modelClass")

        val parameters =
            constructor.parameters
                .map { parameter ->
                    when (parameter.type.classifier) {
                        SavedStateHandle::class -> handle
                        else ->
                            container::class
                                .members
                                .firstOrNull { it.name == parameter.name }
                                ?.call(container)
                                ?: throw IllegalArgumentException("No matching dependency for ${parameter.name}")
                    }
                }.toTypedArray()

        return constructor.call(*parameters)
    }
}
