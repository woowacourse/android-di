package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.di.DependencyContainer

class AutoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val classReflection = modelClass.kotlin
        return DependencyContainer.create(classReflection) as T
    }
}
