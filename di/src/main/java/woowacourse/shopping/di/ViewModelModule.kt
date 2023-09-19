package woowacourse.shopping.di

import android.content.Context
import androidx.lifecycle.ViewModel

open class ViewModelModule(
    applicationContext: Context,
    activityModule: ActivityModule,
) : Module(activityModule) {
    fun <VM : ViewModel> createViewModel(modelClass: Class<VM>): VM {
        return inject(modelClass)
    }
}
