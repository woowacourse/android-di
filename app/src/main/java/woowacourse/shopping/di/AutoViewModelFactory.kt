package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyContainer
import java.util.UUID

class AutoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val classReflection = modelClass.kotlin
        val scope = DependencyContainer.openScope("view-model-${UUID.randomUUID()}")
        return try {
            val viewModel = DependencyContainer.create(classReflection, scope) as T
            if (viewModel is ScopedViewModel) {
                viewModel.dependencyScope = scope
            } else {
                scope.close()
            }
            viewModel
        } catch (exception: Throwable) {
            scope.close()
            throw exception
        }
    }
}
