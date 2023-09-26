package woowacourse.shopping.di.module

import androidx.lifecycle.ViewModel
import woowacourse.shopping.di.container.InstanceContainer

open class ViewModelModule(
    applicationModule: ApplicationModule,
    instanceContainer: InstanceContainer,
) : Module(applicationModule, instanceContainer) {
    fun <VM : ViewModel> createViewModel(modelClass: Class<VM>): VM {
        return inject(modelClass)
    }
}
