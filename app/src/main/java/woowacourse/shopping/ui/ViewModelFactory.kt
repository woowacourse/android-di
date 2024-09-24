package woowacourse.shopping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.woowacourse.di.Injector
import woowacourse.shopping.data.di.ViewModelModule
import kotlin.reflect.KClass

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(
        modelClass: KClass<T>,
        extras: CreationExtras,
    ): T {
        val viewModelModule = ViewModelModule()
        viewModelModule.install()
        return Injector.createInstance(modelClass)
    }
}
