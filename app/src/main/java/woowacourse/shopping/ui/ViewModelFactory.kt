package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.di.DependencyProvider
import com.example.di.inject
import kotlin.reflect.KClass

class ViewModelFactory(private val dependencyProvider: com.example.di.DependencyProvider) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        return com.example.di.inject(modelClass, dependencyProvider)
    }
}
