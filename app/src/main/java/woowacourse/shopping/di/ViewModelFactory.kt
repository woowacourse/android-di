package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(
    private val container: AppContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        val constructor =
            kClass.primaryConstructor
                ?: throw IllegalArgumentException("No primary constructor found for $modelClass")

        val parameters =
            constructor.parameters
                .map { parameter ->
                    container::class
                        .members
                        .first { it.name == parameter.name }
                        .call(container)
                }.toTypedArray()

        return constructor.call(*parameters) as T
    }
}
