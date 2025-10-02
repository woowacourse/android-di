package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun inject(): ViewModelProvider.Factory = ViewModelComponent()

private class ViewModelComponent : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val suitableCtor = modelClass.constructors.firstOrNull { ctor ->
            ctor.parameterTypes.all { paramType ->
                DiSingletonComponent.hasBinding(paramType)
            }
        } ?: modelClass.constructors.maxByOrNull { it.parameterTypes.size }
        ?: throw IllegalArgumentException("No constructors found for ${modelClass.name}")

        val args = suitableCtor.parameterTypes.map { paramType ->
            DiSingletonComponent.matchRaw(paramType)
        }.toTypedArray()

        suitableCtor.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        return suitableCtor.newInstance(*args) as T
    }
}
