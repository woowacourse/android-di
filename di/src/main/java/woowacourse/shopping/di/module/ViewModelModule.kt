package woowacourse.shopping.di.module

import androidx.lifecycle.ViewModel

open class ViewModelModule(
    applicationModule: ApplicationModule,
) : Module(applicationModule) {
    fun <VM : ViewModel> createViewModel(modelClass: Class<VM>): VM {
        return inject(modelClass)
    }
}
