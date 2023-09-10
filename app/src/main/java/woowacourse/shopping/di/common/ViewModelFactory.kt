package woowacourse.shopping.di.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.di.inject.AutoDependencyInjector.inject

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return inject(modelClass.kotlin)
    }
}
