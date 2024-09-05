package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.DependencyInjector

class ViewModelFactory(private val dependencyInjector: DependencyInjector) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val kClass = modelClass.kotlin
        return if (ViewModel::class.java.isAssignableFrom(modelClass)) {
            dependencyInjector.createAndInject(kClass)
        } else {
            throw IllegalArgumentException("${modelClass}은 ViewModel 클래스가 아닙니다.")
        }
    }
}
