package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyContainer
import woowacourse.di.Inject
import kotlin.reflect.full.cast

internal class ReflectionViewModelFactory(
    private val container: DependencyContainer,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = container.create(modelClass.kotlin)
        viewModel.javaClass.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                field.set(viewModel, container.resolve(field.type.kotlin))
            }
        return modelClass.kotlin.cast(viewModel)
    }
}
